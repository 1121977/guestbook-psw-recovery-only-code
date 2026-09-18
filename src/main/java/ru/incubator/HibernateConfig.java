package ru.incubator;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.orm.jpa.hibernate.HibernateTransactionManager;
import org.springframework.orm.jpa.hibernate.LocalSessionFactoryBean;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.incubator.dao.NoteDao;
import ru.incubator.dao.NoteDaoImpl;
import ru.incubator.dao.PasswordResetTokenDao;
import ru.incubator.dao.PasswordResetTokenDaoImpl;
import ru.incubator.service.DbServiceNote;
import ru.incubator.service.DbServiceNoteImpl;
import ru.incubator.service.PasswordResetService;
import ru.incubator.service.PasswordResetServiceImpl;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:hibernate.properties")
public class HibernateConfig {

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
    public NoteDao noteDao(SessionFactory sessionFactory){
        return new NoteDaoImpl(sessionFactory);
    }

    @Bean
    public DbServiceNote dbServiceNote(NoteDao noteDao) {
        return new DbServiceNoteImpl(noteDao);
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(url, username, password);
        dataSource.setDriverClassName(driverClass);
        flywayMigrate(dataSource);
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager hibernateTransactionManager(LocalSessionFactoryBean sessionFactory, DataSource dataSource) {
        HibernateTransactionManager transactionManager
                = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory.getObject());
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    private void flywayMigrate(DataSource dataSource){
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        MigrateResult migrateResult = flyway.migrate();
    }

    @Bean
    public PasswordResetService passwordResetService(PasswordResetTokenDao passwordResetTokenDao,
                                                     @Value("${guestbook.resetpasswordtoken.duration}") Integer resetPasswordTokenDuration){
        return new PasswordResetServiceImpl(passwordResetTokenDao, resetPasswordTokenDuration);
    }

    @Bean
    public PasswordResetTokenDao passwordResetTokenDao(SessionFactory sessionFactory){
        return new PasswordResetTokenDaoImpl(sessionFactory);
    }
}
