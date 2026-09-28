package ru.incubator.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.incubator.HibernateConfig;
import ru.incubator.SecurityConfiguration;
import ru.incubator.dao.PasswordResetTokenDao;
import ru.incubator.model.PasswordResetToken;
import static org.assertj.core.api.Assertions.*;

@SpringJUnitConfig (classes = {HibernateConfig.class, SecurityConfiguration.class})
@ExtendWith(MockitoExtension.class)
class PasswordResetServiceImplTest {

    @Autowired
    PasswordResetService passwordResetService;
    @MockitoBean
    PasswordResetTokenDao passwordResetTokenDao;
    @Captor
    ArgumentCaptor<PasswordResetToken> passwordResetTokenArgumentCaptor;

    @Test
    void createResetTokenTest() {
        when(passwordResetTokenDao.save(any())).thenReturn(0L);
        String returnedToken = passwordResetService.createResetToken("q@a");
        verify(passwordResetTokenDao).save(passwordResetTokenArgumentCaptor.capture());
        assertThat(returnedToken).isNotEqualTo(passwordResetTokenArgumentCaptor.getValue().getToken());
    }
}