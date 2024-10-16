package com.leoric.ecommerceshopbe.security;

import com.leoric.ecommerceshopbe.repositories.SellerRepository;
import com.leoric.ecommerceshopbe.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final String SELLER_PREFIX = "seller_";

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        if (userEmail.startsWith(SELLER_PREFIX)) {
            return sellerRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("Seller " + userEmail + " not found"));
        } else {
            return userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("User " + userEmail + " not found"));
        }
    }

}