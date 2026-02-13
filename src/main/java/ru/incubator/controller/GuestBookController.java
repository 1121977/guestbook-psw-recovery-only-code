package ru.incubator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;
import ru.incubator.model.Note;
import ru.incubator.service.DbServiceNote;

import java.util.List;

@Controller
public class GuestBookController {

    @Autowired
    private DbServiceNote dbServiceNote;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(ModelMap model) {
        List<Note> list = dbServiceNote.findAll();
        model.put("notes", list);
        return "index";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RedirectView saveMessage(@ModelAttribute Note note) {
        note.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        dbServiceNote.saveNote(note);
        return new RedirectView("/", true);
    }
}
