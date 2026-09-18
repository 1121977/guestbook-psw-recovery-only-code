package ru.incubator.dao;

import ru.incubator.model.PasswordResetToken;

public interface PasswordResetTokenDao extends Dao<PasswordResetToken> {
    PasswordResetToken findPasswordResetToken(String email);
    int deletePasswordResetToken(String email);
    long getCountPasswordResetTokenFor(String email);
}
