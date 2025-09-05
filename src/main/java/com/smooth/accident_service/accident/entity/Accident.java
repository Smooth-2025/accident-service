package com.smooth.accident_service.accident.entity;

import com.smooth.accident_service.global.dynamoDb.LocalDateTimeConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import lombok.NonNull;

@Setter
@Getter
@NoArgsConstructor
@DynamoDbBean
public class Accident {

    @NonNull
    private String accidentId;

    private Long vehicleId;

    private BigDecimal latitude;

    private BigDecimal longitude;

    @NonNull
    private LocalDateTime accidentedAt;

    private BigDecimal impulse;

    @NonNull
    private String scale;
    
    private String drivingLog;
    
    @DynamoDbPartitionKey
    public String getPk() {
        return "SCALE#" + scale;
    }

    @DynamoDbSortKey
    public String getSk() {
        return "DATE#" + accidentedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                "#TIME#" + accidentedAt.format(DateTimeFormatter.ofPattern("HH:mm:ss")) +
                "#ID#" + accidentId;
    }

    @DynamoDbConvertedBy(LocalDateTimeConverter.class)
    public LocalDateTime getAccidentedAt() {
        return accidentedAt;
    }

    @DynamoDbAttribute("timeSeriesData")
    public String getDrivingLog() {
        return drivingLog;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "GSI1")
    public String getGsi1pk() {
        return "DATE#" + accidentedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @DynamoDbSecondarySortKey(indexNames = "GSI1")
    public String getGsi1sk() {
        String timeStr = accidentedAt.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return "SCALE#" + scale + "#TIME#" + timeStr;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "GSI2")
    public String getGsi2pk() {
        return "MONTH#" + accidentedAt.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    @DynamoDbSecondarySortKey(indexNames = "GSI2")
    public String getGsi2sk() {
        String dateStr = accidentedAt.format(DateTimeFormatter.ofPattern("dd"));
        String timeStr = accidentedAt.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return "SCALE#" + scale + "#DATE#" + dateStr + "#TIME#" + timeStr;
    }
}
