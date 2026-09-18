package ru.incubator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import ru.incubator.dao.PasswordResetTokenDao;
import ru.incubator.model.PasswordResetToken;

import java.util.List;
import java.util.UUID;

@Transactional(transactionManager = "hibernateTransactionManager")
public class PasswordResetServiceImpl implements PasswordResetService{

    private final PasswordResetTokenDao passwordResetTokenDao;
    private final Integer resetPasswordTokenDuration;

    public PasswordResetServiceImpl(PasswordResetTokenDao passwordResetTokenDao,
                                    Integer resetPasswordTokenDuration) {
        this.passwordResetTokenDao = passwordResetTokenDao;
        this.resetPasswordTokenDuration = resetPasswordTokenDuration;
    }

    @Override
    public boolean verifyToken(String email, String incomingToken) {
        PasswordResetToken token = passwordResetTokenDao.findPasswordResetToken(email);
        if(token == null || token.isExpired()) {
            return false;
        }
        return token.getToken().equals(incomingToken);
    }

    @Override
    public String createResetToken(String email) {
        if (isTokenForEmailExisted(email)){
            deleteToken(email);
        }
        PasswordResetToken passwordResetToken = new PasswordResetToken(UUID.randomUUID().toString(), email, resetPasswordTokenDuration);
        passwordResetTokenDao.save(passwordResetToken);
        return passwordResetToken.getToken();
    }

    @Override
    public int deleteToken(String email) {
        return passwordResetTokenDao.deletePasswordResetToken(email);
    }

    @Override
    public boolean isTokenForEmailExisted(String email) {
        return passwordResetTokenDao.getCountPasswordResetTokenFor(email) > 0;
    }

    public PasswordResetTokenDao getPasswordResetTokenDao() {
        return passwordResetTokenDao;
    }
}
