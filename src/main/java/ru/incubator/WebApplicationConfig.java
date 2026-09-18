package ru.incubator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import ru.incubator.controller.GuestBookController;
import ru.incubator.service.PasswordResetService;

import java.nio.charset.StandardCharsets;

@EnableWebMvc
@Configuration
@ComponentScan
public class WebApplicationConfig implements WebMvcConfigurer {

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.freeMarker();
    }

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        configurer.setTemplateLoaderPath("/WEB-INF/templates");
        configurer.setDefaultCharset(StandardCharsets.UTF_8);
        return configurer;
    }

    @Bean
    public GuestBookController guestBookController(){
        return new GuestBookController();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**", "/static/**", "/script/**")
                .addResourceLocations("classpath:/css/", "classpath:/static/", "classpath:/script/");
    }

}
