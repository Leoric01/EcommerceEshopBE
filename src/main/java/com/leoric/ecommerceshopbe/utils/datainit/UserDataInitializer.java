package com.leoric.ecommerceshopbe.utils.datainit;

import com.leoric.ecommerceshopbe.models.constants.USER_ROLE;
import com.leoric.ecommerceshopbe.requests.dto.AddAddressRequestDTO;
import com.leoric.ecommerceshopbe.security.auth.User;
import com.leoric.ecommerceshopbe.security.auth.UserRepository;
import com.leoric.ecommerceshopbe.services.interfaces.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AddressService addressService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        createUsers();
        createAdminUser();
    }

    private void createUsers() {
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
    }

    private void createAdminUser() {
        String adminEmail = "a@a";
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User userAdmin = User.builder()
                    .email(adminEmail)
                    .password(passwordEncoder.encode("cccc"))
                    .firstName("AdminF 1")
                    .lastName("AdminL 1")
                    .enabled(true)
                    .role(USER_ROLE.ROLE_ADMIN)
                    .build();
            userRepository.save(userAdmin);
        }
    }
}