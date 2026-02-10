package ru.incubator.model;

import jakarta.persistence.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Guest extends User {


    public Guest(String username, @Nullable String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    @Override
    @Id
    public String getUsername(){
        return super.getUsername();
    }

}
