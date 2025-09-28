package com.myrr.CloudStorage.domain.exceptions.conflict;

import com.myrr.CloudStorage.domain.enums.ApiErrorCode;
import com.myrr.CloudStorage.domain.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class ConflictApiException extends BaseException {
    public ConflictApiException(ApiErrorCode code, String message) {
        super(code, HttpStatus.CONFLICT, message);
    }
}
