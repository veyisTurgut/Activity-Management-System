package yte.intern.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import yte.intern.project.util.EmailSender;

import javax.mail.MessagingException;

@SpringBootApplication
//@EnableCaching
//@EnableWebSecurity
public class ProjectApplication {


    public static void main(String[] args) throws MessagingException {

//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        System.out.println(bCryptPasswordEncoder.encode("admin"));
        //$2a$10$j0aYVUaJOsLx1ho9vaPoKOYKMayb7roH0fnyZHmTZVDMFWdI7b2Ae
        SpringApplication.run(ProjectApplication.class, args);

    }
}