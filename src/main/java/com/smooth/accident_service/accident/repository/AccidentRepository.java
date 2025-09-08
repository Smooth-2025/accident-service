package com.smooth.accident_service.accident.repository;

import com.smooth.accident_service.accident.entity.Accident;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class AccidentRepository {

    private final DynamoDbEnhancedClient enhancedClient;

    @Value("${dynamodb.table.name}")
    private String tableName;

    private DynamoDbTable<Accident> table;
    private DynamoDbIndex<Accident> gsi1Index;

    @PostConstruct
    void init() {
        this.table = enhancedClient.table(tableName, TableSchema.fromBean(Accident.class));
        this.gsi1Index = table.index("GSI1");
    }

    public List<Accident> findAll() {
        List<Accident> accidents = table.scan().items().stream().collect(Collectors.toList());
        System.out.println("조회된 사고 데이터 개수: " + accidents.size());
        accidents.forEach(accident -> {
            System.out.println("사고 데이터: " + accident);
            System.out.println("accidentedAt: " + (accident != null ? accident.getAccidentedAt() : "null"));
        });
        return accidents;
    }

    public List<Accident> findAllDesc() {
        return table.scan().items().stream()
                .filter(accident -> accident != null && accident.getAccidentedAt() != null)
                .sorted((a1, a2) -> a2.getAccidentedAt().compareTo(a1.getAccidentedAt()))
                .collect(Collectors.toList());
    }

    public List<Accident> findByDateRange(LocalDate startDate, LocalDate endDate) {

        final String PARTITION_KEY_VALUE = "EVENTS_BY_TIMESTAMP";

        String startTime = "TIME#" + startDate.atStartOfDay().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String endTime = "TIME#" + endDate.atTime(LocalTime.MAX).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        QueryConditional queryConditional = QueryConditional.sortBetween(
                Key.builder()
                        .partitionValue(PARTITION_KEY_VALUE)
                        .sortValue(startTime)
                        .build(),
                Key.builder()
                        .partitionValue(PARTITION_KEY_VALUE)
                        .sortValue(endTime)
                        .build()
        );

        QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .scanIndexForward(false)
                .build();


        return gsi1Index.query(queryRequest)
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public List<Accident> findByScale(String scale) {
        String scaleKey = "SCALE#" + scale;

        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder()
                        .partitionValue(scaleKey)
                        .build());

        QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .scanIndexForward(false)
                .build();

        return table.query(queryRequest)
                .items()
                .stream()
                .collect(Collectors.toList());
    }

    public List<Accident> findByDateRangeAndScale(LocalDate startDate, LocalDate endDate, String scale) {

        String partitionKey = "SCALE#" + scale;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startSortKey = "DATE#" + startDate.format(formatter);
        String endSortKey = "DATE#" + endDate.format(formatter) + "#TIME#23:59:59";

        QueryConditional queryConditional = QueryConditional.sortBetween(
                Key.builder()
                        .partitionValue(partitionKey)
                        .sortValue(startSortKey)
                        .build(),
                Key.builder()
                        .partitionValue(partitionKey)
                        .sortValue(endSortKey)
                        .build()
        );

        QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .scanIndexForward(false)
                .build();

        return table.query(queryRequest)
                .stream()
                .flatMap(page -> page.items().stream())
                .collect(Collectors.toList());
    }
}
