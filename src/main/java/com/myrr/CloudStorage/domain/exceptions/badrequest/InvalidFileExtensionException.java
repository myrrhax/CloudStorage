package com.myrr.CloudStorage.domain.exceptions.badrequest;

import com.myrr.CloudStorage.domain.enums.ApiErrorCode;

public class InvalidFileExtensionException extends BadRequestException {
    public InvalidFileExtensionException() {
        super(ApiErrorCode.INVALID_DATA, "File extension is invalid or not supported");
    }
}
