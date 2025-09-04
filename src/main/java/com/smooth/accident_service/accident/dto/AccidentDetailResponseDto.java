package com.smooth.accident_service.accident.dto;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

import com.smooth.accident_service.accident.entity.AccidentSeverityType;

public record AccidentDetailResponseDto(
    String accidentId,
    UserDto user,
    LocationDto location,
    LocalDateTime accidentAt,
    DecelerationDto deceleration,
    BigDecimal impulse,
    AccidentSeverityType severity,
    EmergencyResponseDto emergencyResponse,
    List<DrivingLogDto> drivingLogs
) {}

record UserDto(
    String userId,
    String userName,
    String vehicleId
) {}

record LocationDto(
    BigDecimal latitude,
    BigDecimal longitude
) {}

record DecelerationDto(
    Double decel,
    Double startSpeed,
    Double endSpeed
) {}

record EmergencyResponseDto(
    Boolean emergencyNotified,
    Boolean familyNotified,
    LocalDateTime reportedAt
) {}

record DrivingLogDto(
    Double speed,
    Double decelerationRate,
    Boolean brakePedal,
    Boolean acceleratorPedal,
    Integer timeOffsetSeconds
) {}