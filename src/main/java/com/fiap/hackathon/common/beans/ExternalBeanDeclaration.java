package com.fiap.hackathon.common.beans;

import com.fiap.hackathon.common.interfaces.external.EmailSender;
import com.fiap.hackathon.external.services.email.EmailSenderImpl;
import com.sendgrid.SendGrid;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalBeanDeclaration {

    @Bean
    public EmailSender emailSender(SendGrid sendGrid) {
        return new EmailSenderImpl(sendGrid);
    }


}
