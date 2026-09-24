package ru.incubator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class PasswordResetToken {
    @Id
    private String userEmail;
    private String token;
    private LocalDateTime expiryDate;

    public PasswordResetToken(){}

    public PasswordResetToken(int expiryInMinutes){
        expiryDate = LocalDateTime.now().plusMinutes(expiryInMinutes);
    }

    public PasswordResetToken(String token, String userEmail, int expiryInMinutes){
        this(expiryInMinutes);
        this.token = token;
        this.userEmail = userEmail;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isExpired() { return LocalDateTime.now().isAfter(expiryDate); }
}

