package ru.incubator.model;

import jakarta.persistence.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.*;

@Entity
@Table(name = "USERS")
public class Guest extends User {

    private List<Long> noteList = new ArrayList<>();

    public Guest(String username, @Nullable String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    @Override
    @Id
    public String getUsername(){
        return super.getUsername();
    }

    public void setUsername(String username){}

    @ElementCollection
    @CollectionTable(
            name = "NOTE",
            joinColumns = @JoinColumn(name = "USERNAME"))
    @Column(name = "ID")
    public List<Long> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<Long> noteList) {
        this.noteList = noteList;
    }
}
