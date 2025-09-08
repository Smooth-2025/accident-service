package com.smooth.accident_service.accident.service;

import com.smooth.accident_service.accident.dto.*;
import com.smooth.accident_service.accident.entity.Accident;
import com.smooth.accident_service.accident.feign.ClientAdapter;
import com.smooth.accident_service.accident.repository.AccidentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.smooth.accident_service.accident.exception.AccidentErrorCode;
import com.smooth.accident_service.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class AccidentServiceImpl implements AccidentService {

    private static final int PAGE_SIZE = 10;

    private final AccidentRepository accidentRepository;
    private final ClientAdapter clientAdapter;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<AccidentDetailResponseDto> getAllAccidents() {
        List<Accident> accidents = accidentRepository.findAll();
        return accidents.stream()
                .filter(accident -> accident != null)
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public AccidentPageResponseDto getAccidents(int page, LocalDate start, LocalDate end, String scale) {

        List<Accident> accidents;

        if (start != null && end != null && scale != null) {
            accidents = accidentRepository.findByDateRangeAndScale(start, end, scale);
        } else if (start != null && end != null) {
            accidents = accidentRepository.findByDateRange(start, end);
        } else if (scale != null) {
            accidents = accidentRepository.findByScale(scale);
        } else {
            accidents = accidentRepository.findAllDesc();
        }

        List<AccidentDetailResponseDto> accidentDtos = accidents.stream()
                .filter(accident -> accident != null)
                .map(this::convertToDto)
                .toList();

        return applyPagination(accidentDtos, page, PAGE_SIZE);
    }

    private AccidentPageResponseDto applyPagination(List<AccidentDetailResponseDto> accidents, int page, int size) {

        int start = page * size;
        int end = Math.min(start + size, accidents.size());

        if (accidents.size() == 0) {
            return AccidentPageResponseDto.of(accidents, page, 0);
        }

        if (start >= accidents.size()) {
            throw new BusinessException(AccidentErrorCode.INVALID_PAGE_NUMBER);
        }

        List<AccidentDetailResponseDto> pageContent = accidents.subList(start, end);

        return AccidentPageResponseDto.of(pageContent, page, accidents.size());
    }

    private AccidentDetailResponseDto convertToDto(Accident accident) {
        return new AccidentDetailResponseDto(
                accident.getAccidentId(),
                convertToUserDto(accident),
                convertToLocationDto(accident),
                accident.getAccidentedAt(),
                null,
                convertToEmergencyResponse(accident),
                accident.getImpulse(),
                accident.getScale(),
                convertDrivingLogs(accident.getDrivingLog())
        );
    }

    private List<DrivingLogDto> convertDrivingLogs(String timeSeriesDataJson) {
        if (timeSeriesDataJson == null || timeSeriesDataJson.isEmpty()) {
            return List.of();
        }

        try {
            JsonNode jsonNode = objectMapper.readTree(timeSeriesDataJson);
            JsonNode preEventArray = jsonNode.get("preEvent");
            
            if (preEventArray == null || !preEventArray.isArray()) {
                return List.of();
            }

            List<DrivingLogDto> drivingLogs = new ArrayList<>();
            for (JsonNode eventNode : preEventArray) {
                DrivingLogDto logDto = new DrivingLogDto(
                        eventNode.has("speed") ? eventNode.get("speed").asDouble() : null,
                        eventNode.has("acceleration") ? eventNode.get("acceleration").asDouble() : null,
                        eventNode.has("brakePressure") ? eventNode.get("brakePressure").asInt() > 0 : null,
                        eventNode.has("acceleratorPressure") ? eventNode.get("acceleratorPressure").asInt() > 0 : null,
                        eventNode.has("offset") ? eventNode.get("offset").asInt() : null
                );
                drivingLogs.add(logDto);
            }
            return drivingLogs;
        } catch (Exception e) {
            return List.of();
        }
    }

    private LocationDto convertToLocationDto(Accident accident) {
        if (accident.getLatitude() == null || accident.getLongitude() == null) {
            return null;
        }
        return new LocationDto(accident.getLatitude(), accident.getLongitude());
    }

    private UserDto convertToUserDto(Accident accident) {
        if (accident.getVehicleId() == null) {
            return null;
        }
        return clientAdapter.getUserInfo(accident.getVehicleId());
    }
    
    private EmergencyResponseDto convertToEmergencyResponse(Accident accident) {
        if (accident.getVehicleId() == null || accident.getAccidentId().trim().isEmpty()) {
            return null;
        }
        return clientAdapter.getEmergencyResponse(accident.getAccidentId());
    }
}
