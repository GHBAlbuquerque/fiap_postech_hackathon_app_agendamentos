package com.fiap.hackathon.external.services.email;

import com.fiap.hackathon.common.interfaces.external.EmailSender;
import com.sendgrid.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class EmailSenderImpl implements EmailSender {

    SendGrid sendGrid;

    @Autowired
    public EmailSenderImpl(SendGrid sendGrid) {
        this.sendGrid = sendGrid;
    }

    private static final Logger logger = LogManager.getLogger(EmailSenderImpl.class);

    @Override
    public void send(Message message) {
        try {
            logger.info("Sending e-mail...");

            final var from = new Email(message.getFrom());
            final var subject = message.getSubject();
            final var to = new Email(message.getTo());
            final var content = new Content("text/plain", message.getText());
            final var mail = new Mail(from, subject, to, content);

            final var request = new Request();

            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            final var response = sendGrid.api(request);

            logger.info("E-mail sent! {}", response.getBody());

        } catch (IOException e) {
            logger.error("Failed to send e-mail...");
            logger.error(e.getMessage());
        }
    }


}
