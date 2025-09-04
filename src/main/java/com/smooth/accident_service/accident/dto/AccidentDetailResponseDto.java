package com.smooth.accident_service.accident.dto;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

public record AccidentDetailResponseDto(
    String accidentId,
    UserDto user,
    LocationDto location,
    LocalDateTime occurredAt,
    String severity,
    DecelerationDto deceleration,
    Integer severityScore,
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
    Double peak,
    Double avg
) {}

record EmergencyResponseDto(
    Boolean emergencyNotified,
    Boolean familyNotified,
    LocalDateTime reportedTime
) {}

record DrivingLogDto(
        Double speed,
        Double decelerationRate,
        Boolean brakePedal,
        Double acceleratorPedal,
        Integer timeOffsetSeconds
) {}