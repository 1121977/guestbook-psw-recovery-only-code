package ru.incubator.security.provisioning;

import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.Collection;

public class JdbcUserDetailsManagerExt extends JdbcUserDetailsManager implements UserDetailsManagerExt{

    public static final String DEF_CREATE_USER_EXT_SQL = "insert into users (username, firstname, lastname, password, enabled) values (?,?,?,?,?)";

    private String createUserSql = DEF_CREATE_USER_EXT_SQL;
    private String createAuthoritySql = DEF_INSERT_AUTHORITY_SQL;
    private JdbcTemplate jdbcTemplate;

    public JdbcUserDetailsManagerExt(){}

    public JdbcUserDetailsManagerExt(DataSource dataSource){
        super(dataSource);
        if (this.jdbcTemplate == null || dataSource != this.jdbcTemplate.getDataSource()) {
            this.jdbcTemplate = new JdbcTemplate(dataSource);
        }
    }

    public void createUser(final UserDetails user, String firstName, String lastName) {
        validateUserDetails(user);
        requireJdbcTemplate().update(this.createUserSql, (ps) -> {
            ps.setString(1, user.getUsername());
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, user.getPassword());
            ps.setBoolean(5, user.isEnabled());
            int paramCount = ps.getParameterMetaData().getParameterCount();
            if (paramCount > 5) {
                // NOTE: acc_locked, acc_expired and creds_expired are also to be inserted
                ps.setBoolean(6, !user.isAccountNonLocked());
                ps.setBoolean(7, !user.isAccountNonExpired());
                ps.setBoolean(8, !user.isCredentialsNonExpired());
            }
        });
        if (getEnableAuthorities()) {
            insertUserAuthorities(user);
        }
    }

    private void validateUserDetails(UserDetails user) {
        Assert.hasText(user.getUsername(), "Username may not be empty or null");
        validateAuthorities(user.getAuthorities());
    }

    private void validateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Authorities list must not be null");
        for (GrantedAuthority authority : authorities) {
            Assert.notNull(authority, "Authorities list contains a null entry");
            Assert.hasText(authority.getAuthority(), "getAuthority() method must return a non-empty string");
        }
    }

    private JdbcTemplate requireJdbcTemplate() {
        JdbcTemplate jdbc = this.jdbcTemplate;
        Assert.notNull(jdbc, "JdbcTemplate cannot be null");
        return jdbc;
    }

    private void insertUserAuthorities(UserDetails user) {
        for (GrantedAuthority auth : user.getAuthorities()) {
            requireJdbcTemplate().update(this.createAuthoritySql, user.getUsername(), auth.getAuthority());
        }
    }


}
