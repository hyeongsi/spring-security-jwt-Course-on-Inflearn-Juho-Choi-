package com.cos.security1.config.auth;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EnvCheck {

    @Value("${GOOGLE_CLIENT_SECRET:NOT_FOUND}")
    private String googleClientSecret;

    @PostConstruct
    public void check() {
        System.out.println("GOOGLE_CLIENT_SECRET: " + (googleClientSecret.equals("NOT_FOUND") ? "없음" : "존재"));
    }
}
