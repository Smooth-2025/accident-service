package com.smooth.accident_service.accident.repository;

import com.smooth.accident_service.accident.entity.Accident;
import com.smooth.accident_service.accident.exception.AccidentErrorCode;
import com.smooth.accident_service.global.exception.BusinessException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.time.LocalDate;
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
        return startDate.datesUntil(endDate.plusDays(1))
                .parallel()
                .flatMap(date -> {
                    String dateKey = "DATE#" + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    QueryConditional queryConditional = QueryConditional
                            .keyEqualTo(Key.builder()
                                    .partitionValue(dateKey)
                                    .build());

                    QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder()
                            .queryConditional(queryConditional)
                            .scanIndexForward(false)
                            .build();

                    return gsi1Index.query(queryRequest)
                            .stream()
                            .flatMap(page -> page.items().stream());
                })
                .collect(Collectors.toList());
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

        return startDate.datesUntil(endDate.plusDays(1))
                .parallel()
                .flatMap(date -> {
                    String dateKey = "DATE#" + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    String scalePrefix = "SCALE#" + scale;

                    QueryConditional queryConditional = QueryConditional
                            .sortBeginsWith(
                                    Key.builder()
                                            .partitionValue(dateKey)
                                            .sortValue(scalePrefix)
                                            .build()
                            );

                    QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder()
                            .queryConditional(queryConditional)
                            .scanIndexForward(false)
                            .build();

                    return gsi1Index.query(queryRequest)
                            .stream()
                            .flatMap(page -> page.items().stream());
                })
                .collect(Collectors.toList());
    }
}
