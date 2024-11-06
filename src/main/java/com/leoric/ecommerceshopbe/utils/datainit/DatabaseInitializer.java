package com.leoric.ecommerceshopbe.utils.datainit;

import com.jayway.jsonpath.JsonPath;
import com.leoric.ecommerceshopbe.models.*;
import com.leoric.ecommerceshopbe.models.constants.HomeCategorySection;
import com.leoric.ecommerceshopbe.models.constants.USER_ROLE;
import com.leoric.ecommerceshopbe.models.embeded.BankDetails;
import com.leoric.ecommerceshopbe.models.embeded.BusinessDetails;
import com.leoric.ecommerceshopbe.repositories.*;
import com.leoric.ecommerceshopbe.requests.dto.AddAddressRequestDTO;
import com.leoric.ecommerceshopbe.security.auth.User;
import com.leoric.ecommerceshopbe.security.auth.UserRepository;
import com.leoric.ecommerceshopbe.services.interfaces.AddressService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DatabaseInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressService addressService;
    private final SellerRepository sellerRepository;
    private final CouponRepository couponRepository;
    private final HomeCategoryRepository homeCategoryRepository;
    private final DealRepository dealRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private int userCount = 0;

    public void initUsers(int numberOfUsers, int addressessPerUser) {
        createDummyUsers(numberOfUsers, addressessPerUser);
    }

    public void initAdmins(int numberOfAdmins) {
        createAdminsAccounts(numberOfAdmins);
    }

    public void initSellers(int numberOfSellers) {
        createSellers(numberOfSellers);
    }

    public void initHomeCategories() {
        createHomeCategories();
    }

    public void initDeals() {
        createDeals();
    }

    public void initCreateCoupons() {
        createCoupons();
    }

    public void initCategories() {
        createCategories();
    }

    public void initProducts(int amountOfSellers) {
        Random rnd = new Random();
        long randomSellerId = rnd.nextLong(1, amountOfSellers);
        createProducts(randomSellerId);
    }

    private void createProducts(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException("Seller not found, id: " + sellerId));

        InputStream productStream = getClass().getResourceAsStream("/initData/products/products.json");
        try {
            List<Map<String, Object>> productsData = JsonPath.read(productStream, "$.products");
            for (Map<String, Object> data : productsData) {
                Product product = new Product();
                product.setTitle((String) data.get("title"));
                product.setDescription((String) data.get("description"));
                product.setMaxPrice((int) data.get("maxPrice"));
                product.setSellingPrice((int) data.get("sellingPrice"));
                product.setColor((String) data.get("color"));
                if (data.get("images") instanceof List<?> tempList) {
                    List<String> imagesList = new ArrayList<>();
                    for (Object o : tempList) {
                        if (o instanceof String image) {
                            imagesList.add(image);
                        }
                    }
                    product.setImage(imagesList);
                }

                Category category1 = categoryRepository.findByCategoryId(data.get("category").toString())
                        .orElseGet(() -> {
                            Category newCategory = new Category();
                            newCategory.setCategoryId(data.get("category").toString());
                            newCategory.setName(data.get("category").toString());
                            newCategory.setLevel(1);
                            return categoryRepository.save(newCategory);
                        });

                Category category2 = categoryRepository.findByCategoryId(data.get("category2").toString())
                        .orElseGet(() -> {
                            Category newCategory = new Category();
                            newCategory.setCategoryId(data.get("category2").toString());
                            newCategory.setName(data.get("category2").toString());
                            newCategory.setLevel(2);
                            newCategory.setParentCategory(category1);
                            return categoryRepository.save(newCategory);
                        });

                Category category3 = categoryRepository.findByCategoryId(data.get("category3").toString())
                        .orElseGet(() -> {
                            Category newCategory = new Category();
                            newCategory.setCategoryId(data.get("category3").toString());
                            newCategory.setName(data.get("category3").toString());
                            newCategory.setLevel(3);
                            newCategory.setParentCategory(category2);
                            return categoryRepository.save(newCategory);
                        });

                product.setCategory(category3);
                product.setSizes((String) data.get("sizes"));
                product.setSeller(seller);

                productRepository.save(product);
            }

            System.out.println("Products loaded successfully.");
        } catch (Exception e) {
            System.err.println("Failed to load products from JSON: " + e.getMessage());
        }
    }

    private void createCategories() {
        if (categoryRepository.count() > 0) {
            System.out.println("categories already are in db(at least 1).");
            return;
        }
        loadCategoriesFromJson("/initData/categories.json", "$.mainCategory", 1);

        // Load Level 2 Categories
        loadCategoriesFromJson("/initData/leveltwo/menLevelTwo.json", "$.menleveltwo", 2);

        // Load Level 3 Categories
        loadCategoriesFromJson("/initData/levelthree/menLevelThree.json", "$.menlevelthree", 3);
    }

    private void loadCategoriesFromJson(String jsonFilePath, String jsonPath, int level) {
        InputStream jsonStream = getClass().getResourceAsStream(jsonFilePath);

        try {
            List<Map<String, Object>> categoriesData = JsonPath.read(jsonStream, jsonPath);
            for (Map<String, Object> data : categoriesData) {
                String categoryId = (String) data.get("categoryId");
                String name = (String) data.get("name");
                String parentCategoryId = (String) data.get("parentCategoryId");

                Category category = new Category();
                category.setName(name);
                category.setCategoryId(categoryId);
                category.setLevel(level);

                if (parentCategoryId != null) {
                    category.setParentCategory(categoryRepository.findByCategoryId(parentCategoryId)
                            .orElseThrow(() -> new IllegalArgumentException("Parent category not found: " + parentCategoryId)));
                }

                categoryRepository.save(category);
            }
            System.out.println("Categories loaded from " + jsonFilePath);
        } catch (Exception e) {
            System.err.println("Failed to load categories from " + jsonFilePath + ": " + e.getMessage());
        }
    }

    private void createCoupons() {
        if (couponRepository.count() == 0) {
            InputStream couponStream = getClass().getResourceAsStream("/initData/coupons.json");

            try {
                List<Map<String, Object>> couponsData = JsonPath.read(couponStream, "$.coupons");

                List<Coupon> coupons = couponsData.stream().map(data -> {
                    String code = (String) data.get("couponCode");
                    double discountPercentage = ((Number) data.get("discount")).doubleValue();
                    LocalDate validityStartDate = LocalDate.parse((String) data.get("validFrom"), DateTimeFormatter.ISO_DATE);
                    LocalDate validityEndDate = LocalDate.parse((String) data.get("validTo"), DateTimeFormatter.ISO_DATE);
                    double minimumOrderValue = ((Number) data.get("maxAmount")).doubleValue();
                    return new Coupon(code, discountPercentage, validityStartDate, validityEndDate, minimumOrderValue);
                }).collect(Collectors.toList());

                couponRepository.saveAll(coupons);
                System.out.println("Coupons loaded successfully.");
            } catch (Exception e) {
                System.err.println("Failed to load coupons from JSON: " + e.getMessage());
            }
        }
    }

    private void createDeals() {
        if (dealRepository.count() == 0) {
            InputStream dealStream = getClass().getResourceAsStream("/initData/deals.json");
            try {
                List<Map<String, Object>> dealsData = JsonPath.read(dealStream, "$.deals");
                List<Deal> deals = dealsData.stream().map(data -> {
                    Integer discount = (Integer) data.get("discount");
                    String categoryId = (String) data.get("categoryId");

                    HomeCategory category = homeCategoryRepository.findFirstByCategoryId(categoryId)
                            .orElseThrow(() -> new IllegalArgumentException("Category with id " + categoryId + " not found"));
                    return new Deal(discount, category);
                }).collect(Collectors.toList());
                dealRepository.saveAll(deals);
                System.out.println("Deals loaded successfully.");
            } catch (Exception e) {
                System.err.println("Failed to load deals from JSON: " + e.getMessage());
            }
        }
    }

    private void createHomeCategories() {
        if (homeCategoryRepository.count() == 0) {
            InputStream inputStream = getClass().getResourceAsStream("/initData/homeCategories.json");
            try {
                List<Map<String, Object>> categoriesData = JsonPath.read(inputStream, "$.homecategories");
                List<HomeCategory> homeCategories = categoriesData.stream().map(data -> {
                    HomeCategory category = new HomeCategory();
                    category.setCategoryId((String) data.get("categoryId"));
                    category.setName((String) data.get("name"));
                    category.setImage((String) data.get("image"));
                    category.setSection(HomeCategorySection.valueOf((String) data.get("section")));
                    return category;
                }).collect(Collectors.toList());
                homeCategoryRepository.saveAll(homeCategories);
                System.out.println("Home categories loaded successfully.");
            } catch (Exception e) {
                System.err.println("Failed to load home categories from JSON: " + e.getMessage());
            }
        }
    }

    private void createSellers(int sellersAmount) {
        for (int i = 1; i <= sellersAmount; i++) {
            int firstIndex = (i - 1) / 9 + 1;
            int secondIndex = (i - 1) % 9 + 1;
            String email = firstIndex + "s@" + secondIndex;

            if (sellerRepository.findByEmail(email).isEmpty()) {
                BankDetails bankDetails = new BankDetails();
                bankDetails.setAccountNumber("ACC" + i);
                bankDetails.setAccountHolderName("Account Holder " + i);
                bankDetails.setIban("IBAN" + i);

                BusinessDetails businessDetails = new BusinessDetails(
                        "BusinessName" + i,
                        "Business Address " + i,
                        "business" + i + "@example.com",
                        "123456789" + i,
                        "logoUrl" + i,
                        "bannerUrl" + i
                );

                Seller seller = Seller.builder()
                        .email(email)
                        .password(passwordEncoder.encode("cccc"))
                        .sellerName("Seller " + i)
                        .VAT(generateVat(getRandomCountryCode()))
                        .isEmailVerified(true)
                        .businessDetails(businessDetails)
                        .bankDetails(bankDetails)
                        .pickupAddress(null)
                        .build();
                sellerRepository.save(seller);

                AddAddressRequestDTO addressDto = new AddAddressRequestDTO(
                        seller.getSellerName() + "'s " + i + " Address",
                        "Street " + i,
                        "Locality " + i,
                        "12345",
                        "City" + i,
                        "Country" + i,
                        "123-456-7890"
                );
                addressService.addSellerAddress(seller.getId(), addressDto);
            }
        }
    }

    private void createDummyUsers(int userAmountToGenerate, int addressAmounPerUser) {
        for (int i = 1 + userCount; i <= userAmountToGenerate + userCount; i++) {
            int firstIndex = (i - 1) / 9 + 1;
            int secondIndex = (i - 1) % 9 + 1;
            String email = firstIndex + "@" + secondIndex;

            if (userRepository.findByEmail(email).isEmpty()) {
                User user = User.builder()
                        .email(email)
                        .password(passwordEncoder.encode("cccc"))
                        .firstName("First" + i)
                        .lastName("Last" + i)
                        .enabled(true)
                        .build();
                userRepository.save(user);
                for (int j = 1; j <= addressAmounPerUser; j++) {
                    AddAddressRequestDTO addressDto = new AddAddressRequestDTO(
                            user.getName() + "'s Address number " + j,
                            "Street " + j,
                            "Locality " + j,
                            "ZIP" + (1000 + j),
                            "City " + j,
                            "Country " + i,
                            "123-456-78" + j
                    );
                    addressService.addUserAddress(user.getId(), addressDto);
                }
            }
        }
        userCount = userCount + userAmountToGenerate;
    }

    private String generateVat(String countryCode) {
        SecureRandom random = new SecureRandom();
        int vatDigitsLength = 8 + random.nextInt(3);
        StringBuilder vatNumber = new StringBuilder(countryCode);
        for (int j = 0; j < vatDigitsLength; j++) {
            vatNumber.append(random.nextInt(10));
        }
        return vatNumber.toString();
    }

    private String getRandomCountryCode() {
        String[] countryCodes = {"DE", "CZ", "FR", "IT", "ES", "NL", "PL", "SE", "FI", "BE", "AT"};
        int randomVAT = new SecureRandom().nextInt(countryCodes.length);
        return countryCodes[randomVAT];
    }

    private void createAdminsAccounts(int adminsCount) {
        if (adminsCount < 10) {
            for (int i = 1; i <= adminsCount; i++) {
                if (userRepository.existsById((long) i)) {
                    adminsCount++;
                    i++;
                    continue;
                }
                String adminEmail = i == 1 ? "a@a" : i + "a@a";
                User userAdmin = User.builder()
                        .email(adminEmail)
                        .password(passwordEncoder.encode("cccc"))
                        .firstName("Admin F " + i)
                        .lastName("Admin ")
                        .enabled(true)
                        .role(USER_ROLE.ROLE_ADMIN)
                        .build();
                userRepository.save(userAdmin);
            }
        }
    }
}