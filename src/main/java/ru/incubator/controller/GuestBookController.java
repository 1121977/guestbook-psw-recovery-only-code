package ru.incubator.controller;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.expression.*;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.jdbc.core.simple.JdbcClient;
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
import ru.incubator.security.provisioning.JdbcUserDetailsManagerExt;
import ru.incubator.service.DbServiceNote;

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
    ApplicationContext applicationContext;
    @Autowired
    DataSource dataSource;

    @RequestMapping(value = "/msg", method = RequestMethod.GET)
    public String msg(@RequestParam(name = "to") String recipient, ModelMap modelMap) {
        modelMap.put("recipient", recipient);
        return "msg";
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(ModelMap model) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Note> list = dbServiceNote.findForUser(username);
        // begin vulnerability
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(new BeanResolver() {
            @Override
            public Object resolve(EvaluationContext context, String beanName) throws AccessException {
                return applicationContext.getBean(beanName);
            }
        });

        String sqlSelectWithParametersRequest = String.format("'.sql(\"SELECT firstname FROM users where username = :username\").param(\"username\", \"%s\")'", username);
        Expression sqlSelectWithParametersExpression = parser.parseExpression(sqlSelectWithParametersRequest);
        String commandString = sqlSelectWithParametersExpression.getValue(context,String.class);
        String expressionString = "T(org.springframework.jdbc.core.simple.JdbcClient).create(@dataSource)" + commandString + ".query(T(String)).optional().orElseThrow()";
        Expression expression = parser.parseExpression(expressionString);
        String firstname = expression.getValue(context, String.class);
        //end vulnerability

//        String firstname = JdbcClient.create(dataSource).sql("SELECT firstname FROM users where username = :username").param("username", username).query(String.class).optional().orElseThrow();

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
    public String regform(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("firstname") String firstname, @RequestParam("lastname") String lastname, ModelMap modelMap) {
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
        ((JdbcUserDetailsManagerExt) userDetailsService).createUser(guest, firstname, lastname);

        modelMap.put("username", username);
        modelMap.put("firstname", firstname);
        modelMap.put("lastname", lastname);
        return "registred";
    }

    @RequestMapping(value = "/deluser", method = RequestMethod.GET)
    public RedirectView deleteUser(){
        ((JdbcUserDetailsManagerExt) userDetailsService).deleteUser(SecurityContextHolder.getContext().getAuthentication().getName());
        return new RedirectView("/logout");
    }

}
