package com.smooth.accident_service.accident.dto;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

public record AccidentDetailResponseDto(
    String accidentId,
    UserDto user,
    LocationDto location,
    LocalDateTime accidentAt,
    EmergencyResponseDto emergencyResponse,
    BigDecimal impulse,
    String scale,
    List<DrivingLogDto> drivingLogs
) {}
