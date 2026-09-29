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
        Session session = sessionFactory.getCurrentSession();
        List<Note> list = session.createQuery("select note from " + entityClass.getName() + " note where note.recipient is NULL or note.recipient = :userName or note.userName = :userName", entityClass)
                .setParameter("userName", userName)
                .getResultList();
        return list;
    }

    @Override
    public int deleteForUser(String userName) {
        if(userName.equals("admin") || userName.equals("checker")){
            return 0;
        }
        Session session = sessionFactory.getCurrentSession();
        int deletedNotes = session.createMutationQuery("delete from " + entityClass.getName() + " where userName = :user or recipient = :user")
                .setParameter("user", userName)
                .executeUpdate();
        return deletedNotes;
    }
}
