package com.myrr.CloudStorage.domain.exceptions.notfound;

import com.myrr.CloudStorage.domain.enums.ApiErrorCode;
import com.myrr.CloudStorage.domain.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public abstract class NotFoundApiException extends BaseException {
    public NotFoundApiException(ApiErrorCode apiCode,
                                String message) {
        super(apiCode, HttpStatus.NOT_FOUND, message);
    }
}