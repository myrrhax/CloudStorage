package com.myrr.CloudStorage.domain.exceptions.badrequest;

import com.myrr.CloudStorage.domain.enums.ApiErrorCode;
import com.myrr.CloudStorage.domain.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public abstract class BadRequestException extends BaseException {
    public BadRequestException(ApiErrorCode code, String message) {
        super(code, HttpStatus.BAD_REQUEST, message);
    }
}
