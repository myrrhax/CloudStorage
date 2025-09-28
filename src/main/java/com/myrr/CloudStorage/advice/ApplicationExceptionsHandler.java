package com.myrr.CloudStorage.advice;

import com.myrr.CloudStorage.domain.dto.ErrorResponseDto;
import com.myrr.CloudStorage.domain.dto.ValidationErrorResponseDto;
import com.myrr.CloudStorage.domain.exceptions.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ApplicationExceptionsHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationExceptionsHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponseDto<String>> handleValidationErrors(MethodArgumentNotValidException exception) {
        var errors = exception.getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
                ));

        return ResponseEntity
                .badRequest()
                .body(new ValidationErrorResponseDto<>(errors));
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponseDto> handle(BaseException exception) {
        ErrorResponseDto responseDto = new ErrorResponseDto(exception.getMessage(), exception.getApiCode());
        HttpStatus statusCode = exception.getStatusCode();

        return new ResponseEntity<>(responseDto, statusCode);
    }
}
