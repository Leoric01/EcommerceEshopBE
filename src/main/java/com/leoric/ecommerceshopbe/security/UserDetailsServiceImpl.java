package com.leoric.ecommerceshopbe.security;

import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.models.User;
import com.leoric.ecommerceshopbe.repositories.SellerRepository;
import com.leoric.ecommerceshopbe.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // First check if it's a user
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            return userOpt.get();
        }

        // If no user found, check if it's a seller
        Optional<Seller> sellerOpt = sellerRepository.findByEmail(email);
        if (sellerOpt.isPresent()) {
            return sellerOpt.get();
        }

        // If neither found, throw an exception
        throw new UsernameNotFoundException("No user or seller found with email " + email);
    }
}
