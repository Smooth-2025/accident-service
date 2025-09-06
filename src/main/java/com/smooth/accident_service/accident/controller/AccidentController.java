package com.smooth.accident_service.accident.controller;

import com.smooth.accident_service.accident.dto.AccidentDetailResponseDto;
import com.smooth.accident_service.accident.service.AccidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/accidents")
@RequiredArgsConstructor
public class AccidentController {

    private final AccidentService accidentService;

    @GetMapping
    public List<AccidentDetailResponseDto> getAllAccidents() {
        return accidentService.getAllAccidents();
    }
}
