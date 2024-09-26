package com.fiap.hackathon.common.interfaces.gateways;

import jakarta.mail.MessagingException;

public interface NotificationGateway {

    void notify(String recipient, String subject, String body) throws MessagingException;
}
