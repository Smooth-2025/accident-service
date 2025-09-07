package com.smooth.accident_service.accident.service;

import com.smooth.accident_service.accident.dto.AccidentDetailResponseDto;
import com.smooth.accident_service.accident.dto.AccidentPageResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface AccidentService {

    List<AccidentDetailResponseDto> getAllAccidents();

    AccidentPageResponseDto getAccidents(int page, LocalDate start, LocalDate end, String scale);
}
