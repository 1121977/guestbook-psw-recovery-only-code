package ru.incubator.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.incubator.model.PasswordResetToken;

import java.util.List;

public class PasswordResetTokenDaoImpl extends DaoImpl<PasswordResetToken> implements PasswordResetTokenDao{


    public PasswordResetTokenDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, PasswordResetToken.class);
    }

    @Override
    public PasswordResetToken findPasswordResetToken(String email) {
        Session session = sessionFactory.getCurrentSession();
        PasswordResetToken passwordResetToken = session.createQuery("select token from " + entityClass.getName() + " token where token.userEmail = :email", entityClass)
                .setParameter("email", email)
                .getSingleResult();
        return passwordResetToken;
    }

    @Override
    public int deletePasswordResetToken(String email) {
        Session session = sessionFactory.getCurrentSession();
        int deletedTokens = session.createMutationQuery("delete from " + entityClass.getName() + " token where token.userEmail = :email")
                .setParameter("email", email)
                .executeUpdate();
        return deletedTokens;
    }

    @Override
    public long getCountPasswordResetTokenFor(String email) {
        Session session = sessionFactory.getCurrentSession();
        long count = session.createQuery("select count(*) from " + entityClass.getName() + " t where t.userEmail = :email", Long.class)
                .setParameter("email", email)
                .getSingleResult();
        return count;
    }
}
