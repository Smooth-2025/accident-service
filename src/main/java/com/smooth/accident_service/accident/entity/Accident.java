package com.smooth.accident_service.accident.entity;

import com.smooth.accident_service.global.dynamoDb.LocalDateTimeConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    @DynamoDbAttribute("PK")
    public String getPk() {
        return "SCALE#" + scale;
    }

    public void setPk(String pk) {

    }

    @DynamoDbSortKey
    @DynamoDbAttribute("SK")
    public String getSk() {
        return "DATE#" + accidentedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                "#TIME#" + accidentedAt.format(DateTimeFormatter.ofPattern("HH:mm:ss")) +
                "#ID#" + accidentId;
    }

    public void setSk(String sk) {

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
    @DynamoDbAttribute("GSI1PK")
    public String getGsi1pk() {
        return "EVENTS_BY_TIMESTAMP";
    }

    public void setGsi1pk(String gsi1pk) {

    }

    @DynamoDbSecondarySortKey(indexNames = "GSI1")
    @DynamoDbAttribute("GSI1SK")
    public String getGsi1sk() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String formattedTimestamp = accidentedAt.format(formatter);
        return "TIME#" + formattedTimestamp;
    }

    public void setGsi1sk(String gsi1sk) {

    }
}
