package ru.incubator.dao;

import org.hibernate.SessionFactory;
import ru.incubator.model.Note;

public class NoteDaoImpl extends DaoImpl<Note> implements NoteDao
{
    public NoteDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Note.class);
    }

}
