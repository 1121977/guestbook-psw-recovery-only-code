package ru.incubator.dao;

import ru.incubator.model.Note;

import java.util.List;

public interface NoteDao extends Dao<Note> {
    List<Note> findForUser(String userName);
    int deleteForUser(String userName);
}
