package com.fiap.hackathon.communication.gateways;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.fiap.hackathon.common.interfaces.external.EmailSender;
import com.fiap.hackathon.external.services.email.Message;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

@ExtendWith(MockitoExtension.class)
class NotificationGatewayImplTest {

    @InjectMocks
    private NotificationGatewayImpl notificationGateway;

    @Mock
    private EmailSender emailSender;

    @Test
    void notify_ShouldSendEmail_WhenParametersAreValid() throws MessagingException {
        final var recipient = "recipient@example.com";
        final var subject = "Test Subject";
        final var body = "Test Body";

        notificationGateway.notify(recipient, subject, body);

        verify(emailSender).send(any(Message.class));
    }

    @Test
    void notify_ShouldThrowMessagingException_WhenEmailSenderThrowsException() throws MessagingException {
        final var recipient = "recipient@example.com";
        final var subject = "Test Subject";
        final var body = "Test Body";
        doThrow(new MessagingException("Error sending email")).when(emailSender).send(any(Message.class));

        final var exception = assertThrows(MessagingException.class, () -> {
            notificationGateway.notify(recipient, subject, body);
        });

        assertEquals("Error sending email", exception.getMessage());
    }
}
