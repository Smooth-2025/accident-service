package com.smooth.accident_service.accident.dto;

import java.util.List;

public record AccidentPageResponseDto(
    List<AccidentDetailResponseDto> content,
    int page,
    int totalPages
) {
    public static AccidentPageResponseDto of(List<AccidentDetailResponseDto> content, int page, int totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / 10);
        return new AccidentPageResponseDto(content, page, totalPages);
    }
}
