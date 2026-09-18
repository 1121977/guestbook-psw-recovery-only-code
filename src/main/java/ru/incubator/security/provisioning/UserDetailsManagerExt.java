package ru.incubator.security.provisioning;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;

public interface UserDetailsManagerExt extends UserDetailsManager {
    void createUser(UserDetails user, String firstName, String lastName, String emailaddress);
    void changeAdminPassword(final String password);
    void changeUserPassword(final String password, String userName);
    String findUserNameByEmail(String email);
}
