package ru.incubator.service;

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
        this.noteDao.save(note);
        return note.getId();
    }


    @Override
    public List<Note> findAll() {
        return noteDao.findAll();
    }
}
