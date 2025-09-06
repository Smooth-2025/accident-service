package com.smooth.accident_service.accident.service;

import com.smooth.accident_service.accident.dto.*;
import com.smooth.accident_service.accident.entity.Accident;
import com.smooth.accident_service.accident.feign.ClientAdapter;
import com.smooth.accident_service.accident.repository.AccidentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class AccidentServiceImpl implements AccidentService {

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
        return clientAdapter.getEmergencyResponse(accident.getAccidentId());
    }
}
