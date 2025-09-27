package com.myrr.CloudStorage.advice;

import com.myrr.CloudStorage.domain.dto.ErrorResponseDto;
import com.myrr.CloudStorage.domain.dto.ValidationErrorResponseDto;
import com.myrr.CloudStorage.domain.enums.ApiErrorCode;
import com.myrr.CloudStorage.domain.exceptions.BaseException;
import com.myrr.CloudStorage.domain.exceptions.notfound.NotFoundApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
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
        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;

        if (exception instanceof NotFoundApiException)
            statusCode = HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(responseDto, statusCode);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handle(Exception exception) {
        LOGGER.error("An error occurred request processing: {}", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDto("Something went wrong", ApiErrorCode.INTERNAL_ERROR));
    }
}
