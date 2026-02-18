package ru.incubator.service;

import ru.incubator.model.Note;
import java.util.List;

public interface DbServiceNote {

    long saveNote(Note note);
    List<Note> findAll();
    List<Note> findForUser(String user);
}
