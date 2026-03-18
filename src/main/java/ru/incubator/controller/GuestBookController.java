package ru.incubator.controller;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.expression.*;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.access.prepost.PreAuthorize;
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
import java.net.http.HttpRequest;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Controller
public class GuestBookController {

    @Autowired
    private DbServiceNote dbServiceNote;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private DataSource dataSource;
    @Autowired
    ApplicationContext applicationContext;

    @RequestMapping(value = "/msg", method = RequestMethod.GET)
    public String msg(@RequestParam(name = "to") String recipient, ModelMap modelMap) {
        modelMap.put("recipient", recipient);
        return "msg";
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(ModelMap model) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Note> list = dbServiceNote.findForUser(username);
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(new BeanResolver() {
            @Override
            public Object resolve(EvaluationContext context, String beanName) throws AccessException {
                return applicationContext.getBean(beanName);
            }
        });
        context.setVariable("username", username);
        String firstnameSelectRequest = String.format("'SELECT firstname FROM users where username = ''%s'''", username);
        Expression firstnameSelectRequestExpression = parser.parseExpression(firstnameSelectRequest);
        String commandString = firstnameSelectRequestExpression.getValue(context,"dataSource", String.class);
        context.setVariable("command", commandString);
        String expressionString = "T(org.springframework.jdbc.core.simple.JdbcClient).create(@dataSource).sql(#command).query(T(String)).optional().orElseThrow()";
        Expression expression = parser.parseExpression(expressionString);
        String firstname = expression.getValue(context, "dataSource", String.class);

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
    public String lcatogin() {
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

}
