package com.fiap.hackathon.common.beans;

import com.fiap.hackathon.common.interfaces.datasources.AppointmentRepository;
import com.fiap.hackathon.common.interfaces.external.EmailSender;
import com.fiap.hackathon.common.interfaces.gateways.AppointmentGateway;
import com.fiap.hackathon.common.interfaces.gateways.NotificationGateway;
import com.fiap.hackathon.communication.gateways.AppointmentGatewayImpl;
import com.fiap.hackathon.communication.gateways.NotificationGatewayImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayBeanDeclaration {

    @Bean
    public AppointmentGateway AppointmentGateway(AppointmentRepository repository) {
        return new AppointmentGatewayImpl(repository);
    }
    

    @Bean
    public NotificationGateway notificationGateway(EmailSender emailSender) {
        return new NotificationGatewayImpl() {
        };
    }
}
