package com.fiap.hackathon.common.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class AWSDynamoBeanDeclaration {

    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient
                .builder()
                .region(Region.US_EAST_1)
                .build();
    }
}