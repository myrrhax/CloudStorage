package com.myrr.CloudStorage.domain.exceptions;

import com.myrr.CloudStorage.domain.enums.ApiErrorCode;
import org.springframework.http.HttpStatus;

public class UnableToDeleteFileException extends BaseException {
    public UnableToDeleteFileException() {
        super(ApiErrorCode.INTERNAL_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An error occurred deleting file");
    }
}
