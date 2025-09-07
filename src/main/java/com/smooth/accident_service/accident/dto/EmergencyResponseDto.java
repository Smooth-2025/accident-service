package com.smooth.accident_service.accident.dto;

import java.time.LocalDateTime;

public record EmergencyResponseDto(
    Boolean emergencyNotified,
    Boolean familyNotified,
    LocalDateTime reportedAt
) {}