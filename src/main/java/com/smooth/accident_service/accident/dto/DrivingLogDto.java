package com.smooth.accident_service.accident.dto;

public record DrivingLogDto(
    Double speed,
    Double decelerationRate,
    Boolean brakePedal,
    Boolean acceleratorPedal,
    Integer timeOffsetSeconds
) {}