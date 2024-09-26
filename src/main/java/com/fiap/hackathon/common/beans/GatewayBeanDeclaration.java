package com.fiap.hackathon.common.beans;

import com.fiap.hackathon.common.interfaces.datasources.AppointmentRepository;
import com.fiap.hackathon.common.interfaces.external.EmailSender;
import com.fiap.hackathon.common.interfaces.gateways.AppointmentGateway;
import com.fiap.hackathon.common.interfaces.gateways.AuthenticationGateway;
import com.fiap.hackathon.common.interfaces.gateways.NotificationGateway;
import com.fiap.hackathon.communication.gateways.AppointmentGatewayImpl;
import com.fiap.hackathon.communication.gateways.AuthenticationGatewayImpl;
import com.fiap.hackathon.communication.gateways.NotificationGatewayImpl;
import com.fiap.hackathon.external.services.users.UsersHTTPClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayBeanDeclaration {

    @Bean
    public AppointmentGateway AppointmentGateway(AppointmentRepository repository, UsersHTTPClient usersHTTPClient) {
        return new AppointmentGatewayImpl(repository, usersHTTPClient);
    }
    

    @Bean
    public NotificationGateway notificationGateway(EmailSender emailSender) {
        return new NotificationGatewayImpl(emailSender) {
        };
    }

    @Bean
    public AuthenticationGateway authenticationGateway(UsersHTTPClient usersHTTPClient){
        return new AuthenticationGatewayImpl(usersHTTPClient);
    }
}
