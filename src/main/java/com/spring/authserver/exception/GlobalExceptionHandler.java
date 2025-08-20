package com.spring.authserver.exception;

import com.spring.authserver.dto.ErrorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponseDto> body(ErrorCode code, String overrideMessage) {
        String message = overrideMessage == null ? code.getDefaultMessage() : overrideMessage;
        return ResponseEntity.status(code.getStatus())
            .body(new ErrorResponseDto(new ErrorResponseDto.ErrorBody(code.name(), message)));
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponseDto> handleApp(AppException e) {
        return body(e.getCode(), e.getMessage());
    }
}
