package com.myrr.CloudStorage.domain.exceptions.unathorized;

import com.myrr.CloudStorage.domain.enums.ApiErrorCode;
import com.myrr.CloudStorage.domain.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public abstract class UnauthorizedApiException extends BaseException {
    public UnauthorizedApiException(ApiErrorCode code, String message) {
        super(code, HttpStatus.UNAUTHORIZED, message);
    }
}
