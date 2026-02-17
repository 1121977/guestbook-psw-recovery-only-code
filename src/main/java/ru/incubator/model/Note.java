package ru.incubator.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Note {

    private long id;
    private String userName;
    private String message;
    private LocalDateTime noteDateTime;
    private String recipient;

    public Note(){
        this.noteDateTime = LocalDateTime.now();
    }

    public Note(String guestName, String message){
        this();
        this.userName = guestName;
        this.message = message;
    }

    @Id
    @GeneratedValue()
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String guestName) {
        this.userName = guestName;
    }

    @Column(length = 1_000)
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

    @Column(nullable = true)
    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}
