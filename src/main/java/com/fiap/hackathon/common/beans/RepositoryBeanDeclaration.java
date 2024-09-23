package com.fiap.hackathon.common.beans;

import com.fiap.hackathon.common.interfaces.datasources.AppointmentRepository;

import com.fiap.hackathon.external.repository.AppointmentRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryBeanDeclaration {

    @Bean
    public AppointmentRepository AppointmentRepository() {
        return new AppointmentRepositoryImpl(dynamoDbClient);
    }
}
