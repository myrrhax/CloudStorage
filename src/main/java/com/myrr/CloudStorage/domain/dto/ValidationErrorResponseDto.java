package com.myrr.CloudStorage.domain.dto;

import com.myrr.CloudStorage.domain.enums.ApiErrorCode;

import java.util.List;
import java.util.Map;

public class ValidationErrorResponseDto<T> extends ErrorResponseDto {
    private final Map<String, List<T>> errors;

    public ValidationErrorResponseDto(Map<String, List<T>> errors) {
        super("Validation exceptions", ApiErrorCode.INVALID_DATA);
        this.errors = errors;
    }

    public Map<String, List<T>> getErrors() {
        return errors;
    }
}
