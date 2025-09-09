package com.smooth.accident_service.accident.dto;

import java.math.BigDecimal;

public record LocationDto(
    BigDecimal latitude,
    BigDecimal longitude
) {}
