package com.smooth.accident_service.accident.dto;

public record UserDto(
    Long userId,
    String userName,
    Long vehicleId
) {}
