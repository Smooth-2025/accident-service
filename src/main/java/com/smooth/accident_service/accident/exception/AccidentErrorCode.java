package com.smooth.accident_service.accident.exception;

import com.smooth.accident_service.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AccidentErrorCode implements ErrorCode {

    INVALID_PAGE_NUMBER(HttpStatus.BAD_REQUEST, 6400, "유효하지 않은 페이지 번호입니다."),
    INVALID_SCALE_VALUE(HttpStatus.BAD_REQUEST, 6401, "유효하지 않은 사고 등급입니다. (medium, high만 허용)"),
    INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, 6402, "종료 날짜는 시작 날짜보다 이전일 수 없습니다."),
    ADMIN_ONLY_ACCESS(HttpStatus.FORBIDDEN, 6403, "관리자만 접근 가능합니다.");

    private final HttpStatus httpStatus;
    private final Integer code;
    private final String message;
}