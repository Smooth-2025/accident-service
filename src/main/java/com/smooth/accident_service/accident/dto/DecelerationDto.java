package com.smooth.accident_service.accident.dto;

public record DecelerationDto(
    Double decel,
    Double startSpeed,
    Double endSpeed
) {}