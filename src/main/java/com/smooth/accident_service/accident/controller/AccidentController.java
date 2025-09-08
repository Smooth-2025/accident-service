package com.smooth.accident_service.accident.controller;

import com.smooth.accident_service.accident.dto.AccidentDetailResponseDto;
import com.smooth.accident_service.accident.dto.AccidentPageResponseDto;
import com.smooth.accident_service.accident.exception.AccidentErrorCode;
import com.smooth.accident_service.accident.service.AccidentService;
import com.smooth.accident_service.global.auth.AuthenticationUtils;
import com.smooth.accident_service.global.common.ApiResponse;
import com.smooth.accident_service.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/accidents")
@RestController
public class AccidentController {

    private final AccidentService accidentService;

    @GetMapping
    public ResponseEntity<ApiResponse<AccidentPageResponseDto>> getAccidents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(required = false) String scale) {

        if (!AuthenticationUtils.isAdmin()) {
            throw new BusinessException(AccidentErrorCode.ADMIN_ONLY_ACCESS);
        }

        if (scale != null && !scale.equals("medium") && !scale.equals("high")) {
            throw new BusinessException(AccidentErrorCode.INVALID_SCALE_VALUE);
        }

        if (start != null && end != null && end.isBefore(start)) {
            throw new BusinessException(AccidentErrorCode.INVALID_DATE_RANGE);
        }

        AccidentPageResponseDto responseDto = accidentService.getAccidents(page, start, end, scale);
        return ResponseEntity.ok(ApiResponse.success("사고 조회가 완료되었습니다.", responseDto));

    }


    @GetMapping("/all")
    public List<AccidentDetailResponseDto> getAllAccidents() {

        if (!AuthenticationUtils.isAdmin()) {
            throw new BusinessException(AccidentErrorCode.ADMIN_ONLY_ACCESS);
        }

        return accidentService.getAllAccidents();
    }
}
