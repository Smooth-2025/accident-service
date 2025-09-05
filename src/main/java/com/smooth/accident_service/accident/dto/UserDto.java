package com.smooth.accident_service.accident.dto;

public record UserDto(
    String userId,
    String userName,
    Long vehicleId
) {}