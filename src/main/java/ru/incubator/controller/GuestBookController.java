package ru.incubator.controller;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.incubator.model.Guest;
import ru.incubator.model.Note;
import ru.incubator.security.provisioning.JdbcUserDetailsManagerExt;
import ru.incubator.service.DbServiceNote;
import ru.incubator.service.PasswordResetService;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.springframework.jdbc.core.JdbcOperationsExtensionsKt.query;

@Controller
public class GuestBookController {

    @Autowired
    private DbServiceNote dbServiceNote;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    DataSource dataSource;
    @Autowired
    private PasswordResetService passwordResetService;

    @RequestMapping(value = "/msg", method = RequestMethod.GET)
    public String msg(@RequestParam(name = "to") String recipient, ModelMap modelMap) {
        modelMap.put("recipient", recipient);
        return "msg";
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(ModelMap model) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Note> list = dbServiceNote.findForUser(username);
        String firstname = JdbcClient.create(dataSource).sql("SELECT firstname FROM users where username = :username").param("username", username).query(String.class).optional().orElseThrow();
        model.put("firstname", firstname);
        model.put("notes", list);
        return "index";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RedirectView saveMessage(@ModelAttribute Note note) {
        note.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        dbServiceNote.saveNote(note);
        return new RedirectView("/", true);
    }

    @RequestMapping(value = "/sendto", method = RequestMethod.POST)
    public RedirectView sendTo(@ModelAttribute Note note) {
        String recipient = note.getRecipient();
        if (recipient != null && ((JdbcUserDetailsManager) userDetailsService).userExists(recipient)) {
            note.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
            dbServiceNote.saveNote(note);
        }
        return new RedirectView("/", true);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/regform", method = RequestMethod.POST)
    public String regform(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("firstname") String firstname, @RequestParam("lastname") String lastname, @RequestParam("emailaddress") String emailaddress, ModelMap modelMap) {
        if (((JdbcUserDetailsManagerExt) userDetailsService).userExists(username)) {
            modelMap.put("username", username);
            return "registration_error";
        }
        Collection<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new GrantedAuthority() {
            @Override
            public @Nullable String getAuthority() {
                return "ROLE_USER";
            }
        });
        Guest guest = new Guest(username, String.format("{bcrypt}%s", new BCryptPasswordEncoder().encode(password)), grantedAuthorities);
        ((JdbcUserDetailsManagerExt) userDetailsService).createUser(guest, firstname, lastname, emailaddress);

        modelMap.put("username", username);
        modelMap.put("firstname", firstname);
        modelMap.put("lastname", lastname);
        return "registred";
    }

    @RequestMapping(value = "/deluser", method = RequestMethod.GET)
    public RedirectView deleteUser() {
        ((JdbcUserDetailsManagerExt) userDetailsService).deleteUser(SecurityContextHolder.getContext().getAuthentication().getName());
        return new RedirectView("/logout");
    }

    @RequestMapping(value = "/repair", method = RequestMethod.POST)
    public RedirectView repair(@RequestParam("email") String email, ModelMap modelMap) {
        return new RedirectView(String.format("/token?token=%s&email=%s", passwordResetService.createResetToken(email), email), true);
    }

    @RequestMapping(value = "/token", method = RequestMethod.GET)
    public String receiveToken(@RequestParam("token") String token, ModelMap modelMap, @RequestParam("email") String email) {
        if (passwordResetService.verifyToken(email, token)) {
            modelMap.put("token", token);
            modelMap.put("email", email);
            return "token_check";
        }
        return "";
    }

    @RequestMapping(value = "/reset_password", method = RequestMethod.POST)
    public String reset_password(@RequestParam("token") String token, @RequestParam("email") String email, @RequestParam("password1") String password, ModelMap modelMap) {
        String username =  ((JdbcUserDetailsManagerExt) userDetailsService).findUserNameByEmail(email);
        if (passwordResetService.verifyToken(email, token)) {
            ((JdbcUserDetailsManagerExt) userDetailsService).changeUserPassword(String.format("{bcrypt}%s", new BCryptPasswordEncoder().encode(password)), username);
            passwordResetService.deleteToken(email);
        }
        modelMap.put("email", email);
        return "password_changed";
    }
}
