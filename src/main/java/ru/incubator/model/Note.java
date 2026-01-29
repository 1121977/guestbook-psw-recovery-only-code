package ru.incubator.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Note {

    @Id
    @GeneratedValue()
    private long id;
    private String userName;
    @Column(length = 1_000)
    private String message;
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date noteDate;
    private LocalDateTime noteDateTime;

    public Note(){
//        this.noteDate = new Date();
        this.noteDateTime = LocalDateTime.now();
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

    public LocalDateTime getNoteDateTime() {
        return noteDateTime;
    }

    public void setNoteDateTime(LocalDateTime noteDate) {
        this.noteDateTime = noteDate;
    }
}
