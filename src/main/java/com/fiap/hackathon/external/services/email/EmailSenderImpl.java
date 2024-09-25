package com.fiap.hackathon.external.services.email;

import com.fiap.hackathon.common.interfaces.external.EmailSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class EmailSenderImpl implements EmailSender {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailSenderImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    //FIXME: Caused by: jakarta.mail.AuthenticationFailedException: 535-5.7.8 Username and Password not accepted. For more information, go to 535 5.7.8  https://support.google.com/mail/?p=BadCredentials

    @Override
    public void send(Message message) throws MessagingException {
        var mimeMessage = criarMimeMessage(message);
        mailSender.send(mimeMessage);
        System.out.printf("message sent");
    }

    protected MimeMessage criarMimeMessage(Message message) throws MessagingException {
        var mimeMessage = mailSender.createMimeMessage();

        final var helper = new MimeMessageHelper(mimeMessage, "UTF-8");

        helper.setSubject(message.getSubject());
        helper.setText(message.getText(), true);
        helper.setTo(message.getTo());
        helper.setFrom(message.getFrom());

        return mimeMessage;
    }
}
