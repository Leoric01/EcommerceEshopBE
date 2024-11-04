package com.leoric.ecommerceshopbe.utils;

import com.leoric.ecommerceshopbe.handler.InvalidAccountTypeAccessException;
import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.repositories.SellerRepository;
import com.leoric.ecommerceshopbe.security.auth.User;
import com.leoric.ecommerceshopbe.security.auth.UserRepository;
import com.leoric.ecommerceshopbe.utils.abstracts.Account;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GlobalUtil {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;

    public static Account getAccountFromPrincipal(Object principal) {
        if (principal == null) {
            throw new IllegalArgumentException("Principal is null");
        }
        if (principal instanceof User) {
            return (User) principal;
        } else if (principal instanceof Seller) {
            return (Seller) principal;
        }
        throw new IllegalArgumentException("Unknown principal type");
    }

    public Account getAccountFromAuthentication(Authentication authentication) {
        if (authentication == null) {
            throw new IllegalArgumentException("No authentication found");
        }
        return getAccountFromPrincipal(authentication.getPrincipal());
    }

    public User getPrincipalAsUser(Authentication authentication) {
        Account account = getAccountFromAuthentication(authentication);
        if (account instanceof User) {
            return userRepository.findById(account.getId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
        }
        throw new InvalidAccountTypeAccessException("Access denied: This endpoint is restricted to User accounts only.");
    }

    public Seller getPrincipalAsSeller(Authentication authentication) {
        Account account = getAccountFromAuthentication(authentication);
        if (account instanceof Seller) {
            return sellerRepository.findById(account.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Seller not found"));
        }
        throw new InvalidAccountTypeAccessException("Your account is not of type Seller");
    }
}