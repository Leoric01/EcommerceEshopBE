package com.leoric.ecommerceshopbe;

import com.jayway.jsonpath.JsonPath;
import com.leoric.ecommerceshopbe.models.Coupon;
import com.leoric.ecommerceshopbe.models.Deal;
import com.leoric.ecommerceshopbe.models.HomeCategory;
import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.models.constants.HomeCategorySection;
import com.leoric.ecommerceshopbe.models.embeded.BankDetails;
import com.leoric.ecommerceshopbe.models.embeded.BusinessDetails;
import com.leoric.ecommerceshopbe.repositories.CouponRepository;
import com.leoric.ecommerceshopbe.repositories.DealRepository;
import com.leoric.ecommerceshopbe.repositories.HomeCategoryRepository;
import com.leoric.ecommerceshopbe.repositories.SellerRepository;
import com.leoric.ecommerceshopbe.requests.dto.AddAddressRequestDTO;
import com.leoric.ecommerceshopbe.security.auth.UserRepository;
import com.leoric.ecommerceshopbe.services.interfaces.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.InputStream;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@EnableAsync
@SpringBootApplication
@RequiredArgsConstructor
public class ECommerceShopBeApplication {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final AddressService addressService;
    private final CouponRepository couponRepository;
    private final HomeCategoryRepository homeCategoryRepository;
    private final DealRepository dealRepository;

    public static void main(String[] args) {
        SpringApplication.run(ECommerceShopBeApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(PasswordEncoder passwordEncoder) {
        return args -> {
            for (int i = 1; i <= 10; i++) {
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
                    String vatCountryCode = generateVat(getRandomCountryCode());

                    Seller seller = Seller.builder()
                            .email(email)
                            .password(passwordEncoder.encode("cccc"))
                            .sellerName("Seller " + i)
                            .VAT(generateVat(vatCountryCode))
                            .isEmailVerified(true)
                            .businessDetails(businessDetails)
                            .bankDetails(bankDetails)
                            .pickupAddress(null)
                            .build();
                    sellerRepository.save(seller);
                    AddAddressRequestDTO addressDto = new AddAddressRequestDTO(
                            "Seller " + i + seller.getSellerName() + " Address",
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
            if (couponRepository.findAll().isEmpty()) {
                Coupon save10 = new Coupon("SAVE10", 10, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), 100);
                Coupon welcome20 = new Coupon("WELCOME20", 20, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 11, 30), 200);
                Coupon holiday30 = new Coupon("HOLIDAY30", 30, LocalDate.of(2024, 3, 15), LocalDate.of(2024, 6, 15), 300);
                Coupon summer15 = new Coupon("SUMMER15", 15, LocalDate.of(2024, 6, 1), LocalDate.of(2024, 9, 1), 150);
                Coupon blackFriday50 = new Coupon("BLACKFRIDAY50", 50, LocalDate.of(2024, 11, 25), LocalDate.of(2024, 11, 30), 500);
                couponRepository.saveAll(Arrays.asList(save10, welcome20, holiday30, summer15, blackFriday50));
            }
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
        };
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
        int randomIndex = new SecureRandom().nextInt(countryCodes.length);
        return countryCodes[randomIndex];
    }
}