package com.leoric.ecommerceshopbe.utils;

import java.util.Random;

public class OtpUtil {

    public static String generateOtp(int length) {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }
}
