package com.fiap.hackathon.common.beans;

import com.fiap.hackathon.common.interfaces.external.EmailSender;
import com.fiap.hackathon.external.services.email.EmailSenderImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class ExternalBeanDeclaration {

    @Bean
    public EmailSender emailSender(){
        return new EmailSenderImpl();
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        //TODO: preencher infos
        final var mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("my.gmail@gmail.com");
        mailSender.setPassword("password");

        final var props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
