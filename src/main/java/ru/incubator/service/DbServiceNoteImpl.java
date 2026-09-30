package ru.incubator.service;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;
import ru.incubator.dao.NoteDao;
import ru.incubator.model.Note;
import java.util.List;

@Transactional(transactionManager = "hibernateTransactionManager")
public class DbServiceNoteImpl implements DbServiceNote {

    private final NoteDao noteDao;

    public DbServiceNoteImpl(NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    @Override
    public long saveNote(Note note) {
        try {
            this.noteDao.save(note);
        } catch (DataAccessException e){
            throw new DbServiceException(e);
        }
        return note.getId();
    }


    @Override
    public List<Note> findAll() {
        return noteDao.findAll();
    }

    @Override
    public List<Note> findForUser(String user) {
        return noteDao.findForUser(user);
    }

    @Override
    public int deleteForUser(String user) {
        return noteDao.deleteForUser(user);
    }
}
