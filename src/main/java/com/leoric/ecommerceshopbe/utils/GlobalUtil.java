package com.leoric.ecommerceshopbe.utils;

import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.security.auth.User;
import com.leoric.ecommerceshopbe.utils.abstracts.Account;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

public class GlobalUtil {

    public static boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static User getPrincipalAsUser(Authentication authentication) {
        Account account = getAccountFromAuthentication(authentication);
        if (account instanceof User) {
            return (User) account;
        }
        throw new BadCredentialsException("Your account is not of type User");
    }

    public static Seller getPrincipalAsSeller(Authentication authentication) {
        Account account = getAccountFromAuthentication(authentication);
        if (account instanceof Seller) {
            return (Seller) account;
        }
        throw new BadCredentialsException("Your account is not of type Seller");
    }

    public static Account getAccountFromAuthentication(Authentication authentication) {
        if (authentication == null) {
            throw new BadCredentialsException("No authentication found");
        }
        return getAccountFromPrincipal(authentication.getPrincipal());
    }

    public static Account getAccountFromPrincipal(Object principal) {
        if (principal == null) {
            throw new BadCredentialsException("Principal is null");
        }
        if (principal instanceof User) {
            return (User) principal;
        } else if (principal instanceof Seller) {
            return (Seller) principal;
        }
        throw new IllegalArgumentException("Unknown principal type");
    }
}
