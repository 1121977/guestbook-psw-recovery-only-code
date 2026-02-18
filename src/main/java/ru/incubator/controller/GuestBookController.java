package ru.incubator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import ru.incubator.model.Note;
import ru.incubator.service.DbServiceNote;

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
    String login() {
        return "login";
    }

}
