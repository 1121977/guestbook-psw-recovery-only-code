package ru.incubator.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.incubator.HibernateConfig;
import ru.incubator.SecurityConfiguration;
import ru.incubator.dao.PasswordResetTokenDao;

import static org.assertj.core.api.Assertions.*;

@SpringJUnitConfig (classes = {HibernateConfig.class, SecurityConfiguration.class})
class PasswordResetServiceImplTest {

    @Autowired
    PasswordResetService passwordResetService;
    @MockitoBean
    PasswordResetTokenDao passwordResetTokenDao;

    @Test
    void createResetTokenTest() {

        assertThat(passwordResetService.createResetToken("q@a")).isEqualTo("qq");
    }
}