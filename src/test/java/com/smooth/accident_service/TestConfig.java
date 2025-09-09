package com.smooth.accident_service;

import com.smooth.accident_service.accident.repository.AccidentRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestConfig {

    @Bean
    public DynamoDbClient dynamoDbClient() {
        return mock(DynamoDbClient.class);
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return mock(DynamoDbEnhancedClient.class);
    }

    @Bean
    public AccidentRepository accidentRepository() {
        return mock(AccidentRepository.class);
    }
}
