package com.spring.authserver.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {
    private ErrorBody error;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorBody {
        private String code;
        private String message;
    }
}
