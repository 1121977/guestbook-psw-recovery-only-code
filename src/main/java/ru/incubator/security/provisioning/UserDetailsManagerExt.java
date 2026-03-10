package ru.incubator.security.provisioning;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;

public interface UserDetailsManagerExt extends UserDetailsManager {
    void createUser(UserDetails user, String firstName, String lastName);
}
