package com.fiap.hackathon.common.beans;

import com.fiap.hackathon.common.interfaces.datasources.AppointmentRepository;
import com.fiap.hackathon.external.repository.AppointmentRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class RepositoryBeanDeclaration {

    @Bean
    public AppointmentRepository AppointmentRepository(DynamoDbClient dynamoDbClient) {
        return new AppointmentRepositoryImpl(dynamoDbClient);
    }
}
