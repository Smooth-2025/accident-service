package com.smooth.accident_service.accident.entity;

import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@Setter
@NoArgsConstructor
@DynamoDbBean
public class DrivingLog {

    private Double speed;
    private Double decelerationRate;
    private Boolean brakePedal;
    private Boolean acceleratorPedal;
    private Integer timeOffsetSeconds;

    @DynamoDbAttribute("speed")
    public Double getSpeed() {
        return speed;
    }

    @DynamoDbAttribute("acceleration")
    public Double getDecelerationRate() {
        return decelerationRate;
    }

    @DynamoDbAttribute("brakePressure")
    public Boolean getBrakePedal() {
        return brakePedal;
    }

    @DynamoDbAttribute("acceleratorPressure")
    public Boolean getAcceleratorPedal() {
        return acceleratorPedal;
    }

    @DynamoDbAttribute("offset")
    public Integer getTimeOffsetSeconds() {
        return timeOffsetSeconds;
    }
}