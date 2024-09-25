package com.fiap.hackathon.communication.gateways;


import com.fiap.hackathon.common.interfaces.external.EmailSender;
import com.fiap.hackathon.common.interfaces.gateways.NotificationGateway;
import com.fiap.hackathon.external.services.email.Message;
import jakarta.mail.MessagingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

public class NotificationGatewayImpl implements NotificationGateway {

    @Value("${spring.mail.username}")
    private String sender;

    private final EmailSender emailSender;

    private static final Logger logger = LogManager.getLogger(NotificationGatewayImpl.class);

    public NotificationGatewayImpl(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void notify(String recipient, String subject, String body) throws MessagingException {
        logger.info("Generating message.");

        final var message = new Message(
                recipient,
                sender,
                subject,
                body
        );

        emailSender.send(message);
    }
}
