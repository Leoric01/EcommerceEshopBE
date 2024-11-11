package com.leoric.ecommerceshopbe.utils.datainit;

import com.jayway.jsonpath.JsonPath;
import com.leoric.ecommerceshopbe.models.*;
import com.leoric.ecommerceshopbe.models.constants.HomeCategorySection;
import com.leoric.ecommerceshopbe.models.constants.USER_ROLE;
import com.leoric.ecommerceshopbe.models.embeded.BankDetails;
import com.leoric.ecommerceshopbe.models.embeded.BusinessDetails;
import com.leoric.ecommerceshopbe.repositories.*;
import com.leoric.ecommerceshopbe.requests.dto.AddAddressRequestDTO;
import com.leoric.ecommerceshopbe.requests.dto.CreateProductReqDto;
import com.leoric.ecommerceshopbe.security.auth.User;
import com.leoric.ecommerceshopbe.security.auth.UserRepository;
import com.leoric.ecommerceshopbe.services.interfaces.AddressService;
import com.leoric.ecommerceshopbe.services.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
@Slf4j
public class DatabaseInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressService addressService;
    private final SellerRepository sellerRepository;
    private final CouponRepository couponRepository;
    private final HomeCategoryRepository homeCategoryRepository;
    private final DealRepository dealRepository;
    private final CategoryRepository categoryRepository;
    private final ProductService productService;
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

    public void initProducts() {

        createProducts();
    }

    private void createProducts() {
        String jsonContent;
        try (InputStream productStream = getClass().getResourceAsStream("/initData/products/products.json")) {
            if (productStream == null) {
                System.err.println("Product JSON file not found.");
                return;
            }
            jsonContent = new String(productStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Failed to read products from JSON: " + e.getMessage());
            return;
        }

        try {
            List<Map<String, Object>> womenProductsData = JsonPath.read(jsonContent, "$.women-products");
            loopThruProductCategory(womenProductsData);
        } catch (Exception e) {
            System.err.println("Failed to load women's products from JSON: " + e.getMessage());
        }
        try {
            System.out.println("Saving FURNITURE products to DB...");
            List<Map<String, Object>> furnitureProductsData = JsonPath.read(jsonContent, "$.furniture-products");
            loopThruProductCategory(furnitureProductsData);
        } catch (Exception e) {
            System.err.println("Failed to load furniture products from JSON: " + e.getMessage());
        }
        try {
            List<Map<String, Object>> menProductsData = JsonPath.read(jsonContent, "$.men-products");
            loopThruProductCategory(menProductsData);
        } catch (Exception e) {
            System.err.println("Failed to load men's products from JSON: " + e.getMessage());
        }
        try {
            List<Map<String, Object>> electronicsProductsData = JsonPath.read(jsonContent, "$.electronics-products");
            loopThruProductCategory(electronicsProductsData);
        } catch (Exception e) {
            System.err.println("Failed to load electronics products from JSON: " + e.getMessage());
        }
    }

    private void loopThruProductCategory(List<Map<String, Object>> productsData) {
        List<Seller> sellers = sellerRepository.findAll();
        for (Map<String, Object> data : productsData) {
            Random rnd = new Random();
            int randomSellerId = rnd.nextInt(0, sellers.size());
            Seller seller = sellers.get(randomSellerId);
            CreateProductReqDto productReq = new CreateProductReqDto();

            productReq.setTitle((String) data.get("title"));
            productReq.setDescription((String) data.get("description"));
            productReq.setMaxPrice((int) data.get("maxPrice"));
            productReq.setSellingPrice((int) data.get("sellingPrice"));
            productReq.setColor((String) data.get("color"));
            productReq.setCategory((String) data.get("category"));
            productReq.setCategory2((String) data.get("category2"));
            productReq.setCategory3((String) data.get("category3"));
            productReq.setSizes((String) data.get("sizes"));
            if (data.get("images") instanceof List<?> tempList) {
                List<String> imagesList = new ArrayList<>();
                for (Object o : tempList) {
                    if (o instanceof String image) {
                        imagesList.add(image);
                    }
                }
                productReq.setImages(imagesList);
            }
            productService.createProduct(productReq, seller);
        }
        log.info("Products loaded successfully.");
    }

    private void createCategories() {
        if (categoryRepository.count() > 200) {
            System.out.println("categories already are in db(at least 1).");
            return;
        }
        loadCategoriesFromJson("/initData/categories.json", "$.mainCategory", 1);

        // Load Level 2 Categories
        loadCategoriesFromJson("/initData/leveltwo/menLevelTwo.json", "$.menleveltwo", 2);
        loadCategoriesFromJson("/initData/leveltwo/womenLevelTwo.json", "$.womenleveltwo", 2);
        loadCategoriesFromJson("/initData/leveltwo/furnitureLevelTwo.json", "$.furnitureleveltwo", 2);
        loadCategoriesFromJson("/initData/leveltwo/electronicsLevelTwo.json", "$.electronicsleveltwo", 2);

        // Load Level 3 Categories
        loadCategoriesFromJson("/initData/levelthree/menLevelThree.json", "$.menlevelthree", 3);
        loadCategoriesFromJson("/initData/levelthree/furnitureLevelThree.json", "$.furniturelevelthree", 3);
        loadCategoriesFromJson("/initData/levelthree/electronicsLevelThree.json", "$.electronicslevelthree", 3);
        loadCategoriesFromJson("/initData/levelthree/womenLevelThree.json", "$.womenlevelthree", 3);
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
                    double minimumOrderValue = ((Number) data.get("minOrderValue")).doubleValue();
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
                        .vat(generateVat(getRandomCountryCode()))
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
        for (int i = 1; i <= adminsCount; i++) {
            String adminEmail = i == 1 ? "a@a" : i + "a@a";
            if (userRepository.existsByEmail(adminEmail)) {
                continue;
            }
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