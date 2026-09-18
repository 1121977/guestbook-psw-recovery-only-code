package ru.incubator.security.provisioning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.util.Assert;
import ru.incubator.model.Guest;
import ru.incubator.service.DbServiceNote;
import javax.sql.DataSource;
import java.util.Collection;

public class JdbcUserDetailsManagerExt extends JdbcUserDetailsManager implements UserDetailsManagerExt {

    public static final String DEF_CREATE_USER_EXT_SQL = "insert into users (username, firstname, lastname, password, enabled, emailaddress) values (?,?,?,?,?,?)";
    public static final String DEF_FIND_USERNAME_BY_EMAIL = "select username from users where emailaddress = ?";
    private String createUserSql = DEF_CREATE_USER_EXT_SQL;
    private String createAuthoritySql = DEF_INSERT_AUTHORITY_SQL;
    private String changePasswordSql = DEF_CHANGE_PASSWORD_SQL;
    private String findUsernameByEmail = DEF_FIND_USERNAME_BY_EMAIL;
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DbServiceNote dbServiceNote;

    public JdbcUserDetailsManagerExt() {
    }

    public JdbcUserDetailsManagerExt(DataSource dataSource) {
        super(dataSource);
        if (this.jdbcTemplate == null || dataSource != this.jdbcTemplate.getDataSource()) {
            this.jdbcTemplate = new JdbcTemplate(dataSource);
        }
    }

    public void createUser(final UserDetails user, String firstName, String lastName, String emailaddress) {
        validateUserDetails(user);
        requireJdbcTemplate().update(this.createUserSql, (ps) -> {
            ps.setString(1, user.getUsername());
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, user.getPassword());
            ps.setBoolean(5, user.isEnabled());
            ps.setString(6, emailaddress);
            int paramCount = ps.getParameterMetaData().getParameterCount();
            if (paramCount > 6) {
                ps.setBoolean(7, !user.isAccountNonLocked());
                ps.setBoolean(8, !user.isAccountNonExpired());
                ps.setBoolean(9, !user.isCredentialsNonExpired());
            }
        });
        if (getEnableAuthorities()) {
            insertUserAuthorities(user);
        }
    }

    @Override
    public void changeAdminPassword(String newPassword) {
        requireJdbcTemplate().update(this.changePasswordSql, newPassword, "admin");
    }

    @Override
    public void changeUserPassword(String newPassword, String userName) {
        int number = requireJdbcTemplate().update(this.changePasswordSql, newPassword, userName);
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

    @Override
    public void deleteUser(String username) {
        dbServiceNote.deleteForUser(username);
        super.deleteUser(username);
    }

    public String findUserNameByEmail(String email){
        return requireJdbcTemplate().queryForObject(findUsernameByEmail, String.class, email);
    }

}
