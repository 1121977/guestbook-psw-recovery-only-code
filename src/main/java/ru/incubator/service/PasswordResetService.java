package ru.incubator.service;

public interface PasswordResetService {
    boolean verifyToken(String email, String incomingToken);
    String createResetToken(String email);
    int deleteToken(String email);
    boolean isTokenForEmailExisted(String email);
}
