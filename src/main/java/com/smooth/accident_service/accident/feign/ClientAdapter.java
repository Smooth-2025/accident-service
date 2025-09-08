package com.smooth.accident_service.accident.feign;

import com.smooth.accident_service.accident.dto.UserDto;
import com.smooth.accident_service.accident.dto.EmergencyResponseDto;
import com.smooth.accident_service.global.common.ApiResponse;
import com.smooth.accident_service.global.exception.BusinessException;
import com.smooth.accident_service.global.exception.CommonErrorCode;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClientAdapter {
    
    private final UserServiceClient userServiceClient;
    private final EmergencyServiceClient emergencyServiceClient;
    
    public UserDto getUserInfo(Long userId) {
        try {
            return userServiceClient.getUserInfo(userId);
        } catch (FeignException e) {
            throw new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR,
                "유저 서비스 통신 오류: " + e.getMessage());
        }
    }
    
    public EmergencyResponseDto getEmergencyResponse(String accidentId) {
        try {
            ApiResponse<EmergencyResponseDto> response = emergencyServiceClient.getAccidentById(accidentId);
            if (!response.isSuccess()) {
                return EmergencyResponseDto.empty();
            }
            return response.getData() != null ? response.getData() : EmergencyResponseDto.empty();
        } catch (FeignException e) {
            return EmergencyResponseDto.empty();
        }
    }
}
