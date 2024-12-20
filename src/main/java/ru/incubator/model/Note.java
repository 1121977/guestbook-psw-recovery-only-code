package ru.incubator.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Note {

    @Id
    @GeneratedValue()
    private long id;
    private String userName;
    @Column(length = 1_000)
    private String message;
    @Temporal(TemporalType.TIMESTAMP)
    private Date noteDate;

    public Note(){
        this.noteDate = new Date();
    }

    public Note(String userName, String message){
        this();
        this.userName = userName;
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(Date noteDate) {
        this.noteDate = noteDate;
    }
}
