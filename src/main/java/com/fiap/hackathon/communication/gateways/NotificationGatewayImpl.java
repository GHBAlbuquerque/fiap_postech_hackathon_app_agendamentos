package com.fiap.hackathon.communication.gateways;


import com.fiap.hackathon.common.interfaces.external.EmailSender;
import com.fiap.hackathon.common.interfaces.gateways.NotificationGateway;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NotificationGatewayImpl implements NotificationGateway {

    private final EmailSender emailSender;

    private static final Logger logger = LogManager.getLogger(NotificationGatewayImpl.class);

    public NotificationGatewayImpl(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void notify(String email, String message) {

    }
}
