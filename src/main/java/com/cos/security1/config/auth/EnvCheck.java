package com.cos.security1.config.auth;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EnvCheck {

    @Value("${GOOGLE_CLIENT_SECRET:NOT_FOUND}")
    private String googleClientSecret;

    @Value("${NAVER_CLIENT_SECRET:NOT_FOUND}")
    private String naverClientSecret;

    @PostConstruct
    public void check() {
        System.out.println("GOOGLE_CLIENT_SECRET: " + (googleClientSecret.equals("NOT_FOUND") ? "없음" : "존재"));
        System.out.println("NAVER_CLIENT_SECRET: " + (naverClientSecret.equals("NOT_FOUND") ? "없음" : "존재"));
    }
}
