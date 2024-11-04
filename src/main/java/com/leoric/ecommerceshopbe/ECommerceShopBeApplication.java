package com.leoric.ecommerceshopbe;

import com.leoric.ecommerceshopbe.models.Coupon;
import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.models.constants.USER_ROLE;
import com.leoric.ecommerceshopbe.models.embeded.BankDetails;
import com.leoric.ecommerceshopbe.models.embeded.BusinessDetails;
import com.leoric.ecommerceshopbe.repositories.CouponRepository;
import com.leoric.ecommerceshopbe.repositories.SellerRepository;
import com.leoric.ecommerceshopbe.requests.dto.AddAddressRequestDTO;
import com.leoric.ecommerceshopbe.security.auth.User;
import com.leoric.ecommerceshopbe.security.auth.UserRepository;
import com.leoric.ecommerceshopbe.services.interfaces.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Arrays;

@EnableAsync
@SpringBootApplication
@RequiredArgsConstructor
public class ECommerceShopBeApplication {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final AddressService addressService;
    private final CouponRepository couponRepository;
    public static void main(String[] args) {
        SpringApplication.run(ECommerceShopBeApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(PasswordEncoder passwordEncoder) {
        return args -> {
            for (int i = 1; i <= 20; i++) {
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
                    for (int j = 1; j <= 4; j++) {
                        AddAddressRequestDTO addressDto = new AddAddressRequestDTO(
                                "User " + i + " Address " + j,
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
            String adminEmail = "a@a";
            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                User userAdmin = User.builder()
                        .email("a@a")
                        .password(passwordEncoder.encode("cccc"))
                        .firstName("AdminF 1")
                        .lastName("AdminL 1")
                        .enabled(true)
                        .role(USER_ROLE.ROLE_ADMIN)
                        .build();
                userRepository.save(userAdmin);
            }

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
            Coupon save10 = new Coupon("SAVE10", 10, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), 100);
            Coupon welcome20 = new Coupon("WELCOME20", 20, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 11, 30), 200);
            Coupon holiday30 = new Coupon("HOLIDAY30", 30, LocalDate.of(2024, 3, 15), LocalDate.of(2024, 6, 15), 300);
            Coupon summer15 = new Coupon("SUMMER15", 15, LocalDate.of(2024, 6, 1), LocalDate.of(2024, 9, 1), 150);
            Coupon blackFriday50 = new Coupon("BLACKFRIDAY50", 50, LocalDate.of(2024, 11, 25), LocalDate.of(2024, 11, 30), 500);
            couponRepository.saveAll(Arrays.asList(save10, welcome20, holiday30, summer15, blackFriday50));
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