package com.myrr.CloudStorage.domain.exceptions;

import com.myrr.CloudStorage.domain.enums.ApiErrorCode;
import org.springframework.http.HttpStatus;

public abstract class BaseException extends RuntimeException {
    protected ApiErrorCode apiCode;
    private HttpStatus statusCode;

    public BaseException(ApiErrorCode code,
                         HttpStatus statusCode,
                         String message) {
        super(message);
        this.apiCode = code;
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public ApiErrorCode getApiCode() {
        return apiCode;
    }
}
