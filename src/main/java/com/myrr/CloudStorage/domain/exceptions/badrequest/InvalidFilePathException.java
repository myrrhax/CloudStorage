package com.myrr.CloudStorage.domain.exceptions.badrequest;

import com.myrr.CloudStorage.domain.enums.ApiErrorCode;

public class InvalidFilePathException extends BadRequestException {
    public InvalidFilePathException() {
        super(ApiErrorCode.INVALID_DATA, "Invalid file full path");
    }
}
