package com.cos.security1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String password;
    private String username;
    private String email;
    private String role; // ROLE_USER, ROLE_ADMIN

    private String provider; // google
    private String providerId; // google sub id
    @CreationTimestamp
    private Timestamp createDate;

    @Builder
    public User(String password, String username, String email, String role, String provider, String providerId, Timestamp createDate) {
        this.password = password;
        this.username = username;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.createDate = createDate;
    }

    public User() {
    }
}
