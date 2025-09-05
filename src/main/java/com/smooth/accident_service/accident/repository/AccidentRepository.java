package com.smooth.accident_service.accident.repository;

import com.smooth.accident_service.accident.entity.Accident;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AccidentRepository {

    private final DynamoDbEnhancedClient enhancedClient;
    
    @Value("${dynamodb.table.name}")
    private String tableName;

    private DynamoDbTable<Accident> table;

    @PostConstruct
    void init() {
        this.table = enhancedClient.table(tableName, TableSchema.fromBean(Accident.class));
    }

    public List<Accident> findAll() {
        return table.scan().items().stream().collect(Collectors.toList());
    }
    // TODO : 쿼리 이용
}
