package com.smooth.accident_service.global.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    Integer getCode();
    String getMessage();
    HttpStatus getHttpStatus();
}
