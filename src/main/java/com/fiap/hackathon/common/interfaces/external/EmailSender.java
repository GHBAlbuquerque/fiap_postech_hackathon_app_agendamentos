package com.fiap.hackathon.common.interfaces.external;

import com.fiap.hackathon.external.services.email.Message;
import jakarta.mail.MessagingException;

public interface EmailSender {

    void send(Message message) throws MessagingException;
}
