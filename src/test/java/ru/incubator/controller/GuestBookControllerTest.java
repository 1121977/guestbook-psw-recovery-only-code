package ru.incubator.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.incubator.model.Guest;
import ru.incubator.service.DbServiceNote;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.mockito.Mockito.when;

class GuestBookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DbServiceNote dbServiceNote;
    @Mock
    private JdbcUserDetailsManager jdbcUserDetailsManager;
/*
    @Mock(extraInterfaces = {UserDetailsManager.class})
    private UserDetailsService userDetailsService;
*/
    @InjectMocks
    private GuestBookController guestBookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = standaloneSetup(guestBookController).build();
    }

    @Test
    void regformTest() throws Exception {
//        doNothing().when(((JdbcUserDetailsManager) userDetailsService)).createUser(any(Guest.class));
        doNothing().when(jdbcUserDetailsManager).createUser(any(Guest.class));
//        var post = post("/regform");
//        var accept = post.accept(MediaType.TEXT_PLAIN);
//        accept.param("username", "SomeValue").param("password", "qwerty12");
        var a = mockMvc.perform(post("/regform")
                        .accept(MediaType.TEXT_PLAIN)
                        .param("username", "SomeValue")
                        .param("password", "qwerty12")
                );
        System.out.printf("Result is:\n %s", a.andReturn().getResponse().getContentAsString());
        a.andExpect(status().isOk());
    }

    @Test
    void index() throws Exception{
        var get = get("/");
        var a = mockMvc.perform(get);
        a.andExpect(status().isOk());
    }
}