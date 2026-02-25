package ru.incubator.dao;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.hibernate.HibernateTransactionManager;
import org.springframework.orm.jpa.hibernate.LocalSessionFactoryBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.incubator.model.Note;
import ru.incubator.service.DbServiceNote;
import ru.incubator.service.DbServiceNoteImpl;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import javax.sql.DataSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
class NoteDaoImplTest {

    @Autowired
    NoteDao noteDao;

    @Autowired
    DbServiceNote dbServiceNote;

//    @Test
    void saveTest() {
        Note note = new Note();
        note.setMessage("Hello, test!");
        note.setUserName("King-kong");
        assertDoesNotThrow(() -> noteDao.save(note));
    }

//    @Test
    void getAllNotesTest() {
        List<Note> allNoteList = noteDao.findAll();
        allNoteList.stream().forEach(note -> System.out.println("Note's time is " + note.getNoteDateTime()));
        assertTrue(!allNoteList.isEmpty());
    }

//    @Test
    void getAllNotesTest2() {
        List<Note> allNoteList = dbServiceNote.findAll();
        allNoteList.forEach(note -> System.out.println("Note's time is " + note.getNoteDateTime()));
        assertTrue(!allNoteList.isEmpty());
    }


    @EnableTransactionManagement
    @Configuration
    @PropertySource("classpath:hibernate.properties")
    static public class NoteDaoImplTestConf {

        @Value("${hibernate.connection.driver_class}")
        private String driverClass;
        @Value("${hibernate.connection.url}")
        private String url;
        @Value("${hibernate.connection.username}")
        private String username;
        @Value("${hibernate.connection.password}")
        private String password;


        @Bean
        public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
            LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
            sessionFactory.setDataSource(dataSource);
            sessionFactory.setPackagesToScan("ru.incubator.model");
            sessionFactory.setConfigLocation(new ClassPathResource("hibernate.cfg.xml"));

            return sessionFactory;
        }

        @Bean
        public NoteDao noteDao(SessionFactory sessionFactory) {
            return new NoteDaoImpl(sessionFactory);
        }

        @Bean
        public PlatformTransactionManager hibernateTransactionManager(LocalSessionFactoryBean sessionFactory) {
            HibernateTransactionManager transactionManager
                    = new HibernateTransactionManager();
            transactionManager.setSessionFactory(sessionFactory.getObject());
            return transactionManager;
        }

        @Bean
        public DbServiceNote dbServiceNote(NoteDao noteDao) {
            return new DbServiceNoteImpl(noteDao);
        }

        @Bean
        public DataSource dataSource() {
            DriverManagerDataSource dataSource = new DriverManagerDataSource(url, username, password);
            dataSource.setDriverClassName(driverClass);
            return dataSource;
        }

    }

}