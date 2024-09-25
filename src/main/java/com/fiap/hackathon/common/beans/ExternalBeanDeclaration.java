package com.fiap.hackathon.common.beans;

import com.fiap.hackathon.common.interfaces.external.EmailSender;
import com.fiap.hackathon.external.services.email.EmailSenderImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;


@Configuration
public class ExternalBeanDeclaration {

    @Bean
    public EmailSender emailSender(JavaMailSender javaMailSender) {
        return new EmailSenderImpl(javaMailSender);
    }

}
