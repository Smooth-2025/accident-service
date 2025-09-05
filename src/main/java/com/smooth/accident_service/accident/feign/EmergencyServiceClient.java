package com.smooth.accident_service.accident.feign;

import com.smooth.accident_service.accident.dto.EmergencyResponseDto;
import com.smooth.accident_service.global.common.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "drivecast-service", url = "${app.client.drivecast-service.url}", path = "internal/v1/accidents")
public interface EmergencyServiceClient {
    @GetMapping("/{accidentId}/emergency")
    ApiResponse<EmergencyResponseDto> getAccidentById(@PathVariable("accidentId") String accidentId);
}
