package com.myrr.CloudStorage.domain.dto;

import com.myrr.CloudStorage.domain.enums.ApiErrorCode;

public class ErrorResponseDto {
    private ApiErrorCode code;
    private String message;

    public ErrorResponseDto(String message, ApiErrorCode code) {
        this.message = message;
        this.code = code;
    }

    public ApiErrorCode getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
};
