package com.myrr.CloudStorage.domain.exceptions;

import com.myrr.CloudStorage.domain.enums.ApiErrorCode;
import org.springframework.http.HttpStatus;

public class UnableToLoadFileException extends BaseException {
    public UnableToLoadFileException() {
        super(ApiErrorCode.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, "Failed to load file to server. Try again later");
    }
}
