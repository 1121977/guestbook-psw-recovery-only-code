package ru.incubator.service.security.provisioning;

import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

public class JdbcGuestDetailsManager extends JdbcUserDetailsManager {
    public static final String DEF_CREATE_GUEST_SQL = "insert into guests (username, password, enabled) values (?,?,?)";

    public JdbcGuestDetailsManager(DataSource dataSource){
        super(dataSource);
    }
}
