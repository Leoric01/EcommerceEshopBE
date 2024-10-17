package com.leoric.ecommerceshopbe.utils;

import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.models.User;
import com.leoric.ecommerceshopbe.utils.abstracts.Account;

public class GlobalUtil {

    public static boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static Account getAccountFromPrincipal(Object principal) {
        if (principal instanceof User) {
            return (User) principal;
        } else if (principal instanceof Seller) {
            return (Seller) principal;
        }
        throw new IllegalArgumentException("Unknown principal type");
    }
}
