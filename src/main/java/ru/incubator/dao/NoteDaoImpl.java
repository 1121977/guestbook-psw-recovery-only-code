package ru.incubator.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.incubator.model.Note;

import java.util.List;

public class NoteDaoImpl extends DaoImpl<Note> implements NoteDao
{
    public NoteDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Note.class);
    }

    @Override
    public List<Note> findForUser(String userName) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        List<Note> list = session.createQuery("select note from " + entityClass.getName() + " note where note.recipient is NULL or note.recipient = :recipientName", entityClass)
                .setParameter("recipientName", userName)
                .getResultList();
        tx.commit();
        session.close();
        return list;
    }
}
