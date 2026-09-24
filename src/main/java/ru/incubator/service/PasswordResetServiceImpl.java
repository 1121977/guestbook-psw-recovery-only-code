package ru.incubator.service;

import org.springframework.transaction.annotation.Transactional;
import ru.incubator.dao.PasswordResetTokenDao;
import ru.incubator.model.PasswordResetToken;
import java.util.UUID;

@Transactional(transactionManager = "hibernateTransactionManager")
public class PasswordResetServiceImpl implements PasswordResetService{

    private final PasswordResetTokenDao passwordResetTokenDao;
    private final Integer resetPasswordTokenDuration;
    //private final MailSubsystemService mailSubsystemService; // Необходима для отправки сообщений электронной почты.

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

/*
 * Для рабочего варианта сервиса достаточно возвращаемого значения типа void. После проведения проверок на наличие
 * пользователя с указанным адресом электронной почты, производится отправка сообщения, содержащего ссылку с токеном.
 * Реализация MailSubsystemService в упражнении не приведена, но она может быть реализована, например, на основании
 * javax.mail.
 *
 *     @Async // должно быть аннотировано для промышленной эксплуатации (см. комментарии к GuestBookController.repair(String email)
 *     @Override
 *     public String createResetToken(String email) {
 *         if (isTokenForEmailExisted(email)){
 *             deleteToken(email);
 *         }
 *         PasswordResetToken passwordResetToken = new PasswordResetToken(UUID.randomUUID().toString(), email, resetPasswordTokenDuration);
 *         passwordResetTokenDao.save(passwordResetToken);
 *         mailSubsystemService.sendEmail(email, passwordResetToken);
 *     }
 *
 */
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
