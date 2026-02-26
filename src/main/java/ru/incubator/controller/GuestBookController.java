package ru.incubator.controller;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import ru.incubator.model.Guest;
import ru.incubator.model.Note;
import ru.incubator.service.DbServiceNote;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Controller
public class GuestBookController {

    @Autowired
    private DbServiceNote dbServiceNote;
    @Autowired
    private UserDetailsService userDetailsService;

    @RequestMapping(value = "/msg", method = RequestMethod.GET)
    public String msg(@RequestParam(name = "to") String recipient, ModelMap modelMap) {
        modelMap.put("recipient", recipient);
        return "msg";
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(ModelMap model) {
//        List<Note> list = dbServiceNote.findAll();
        List<Note> list = dbServiceNote.findForUser(SecurityContextHolder.getContext().getAuthentication().getName());
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
    public String regform(@RequestParam("username") String username, @RequestParam("password") String password, ModelMap modelMap) {
        Collection<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new GrantedAuthority() {
            @Override
            public @Nullable String getAuthority() {
                return "ROLE_USER";
            }
        });
        Guest guest = new Guest(username, String.format("{bcrypt}%s", new BCryptPasswordEncoder().encode(password)), grantedAuthorities);
        ((JdbcUserDetailsManager) userDetailsService).createUser(guest);
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("findAll()");
        modelMap.put("username", expression.getValue(dbServiceNote));
        return "registred";
    }

}
