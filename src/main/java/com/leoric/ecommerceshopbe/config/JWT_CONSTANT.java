package com.leoric.ecommerceshopbe.config;


public class JWT_CONSTANT {
    public static final String JWT_HEADER = "Authorization";

    public static String getSecretKey() {
        return JwtConfig.SECRET_KEY;
    }
}
