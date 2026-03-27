package ru.incubator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.incubator.security.provisioning.JdbcUserDetailsManagerExt;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application.properties")
@EnableWebSecurity(debug = false)
public class SecurityConfiguration {

    @Value("${guestbook.admin.password}")
    private String adminPasswordHash;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/css/**", "/static/**", "/regform", "/script/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")
                                .permitAll());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        var users = new JdbcUserDetailsManagerExt(dataSource);
        if (!users.userExists("admin")) {
            UserDetails admin = User.builder()
                    .username("admin")
                    .password(adminPasswordHash)
                    .roles("ADMIN")
                    .build();
            users.createUser(admin, "Admin", "Admin");
        } else {
            users.changeAdminPassword(adminPasswordHash);
        }
        return users;
    }

}
