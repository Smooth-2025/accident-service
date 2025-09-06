package com.smooth.accident_service.accident.feign;

import com.smooth.accident_service.accident.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${app.client.user-service.url}", path = "internal/v1/users")
public interface UserServiceClient {
    
    @GetMapping("/{userId}/admin-info")
    UserDto getUserInfo(@PathVariable("userId") Long userId);
}
