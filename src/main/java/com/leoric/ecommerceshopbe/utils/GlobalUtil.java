package com.leoric.ecommerceshopbe.utils;

public class GlobalUtil {

    public static boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }
}
