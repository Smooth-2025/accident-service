package com.smooth.accident_service.accident.service;

import com.smooth.accident_service.accident.dto.AccidentDetailResponseDto;

import java.util.List;

public interface AccidentService {
    List<AccidentDetailResponseDto> getAllAccidents();
}
