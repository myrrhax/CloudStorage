package com.myrr.CloudStorage.domain.exceptions.badrequest;

import com.myrr.CloudStorage.domain.enums.ApiErrorCode;

public class FileCannotBeNullException extends BadRequestException {
    public FileCannotBeNullException() {
        super(ApiErrorCode.INVALID_DATA, "Uploaded file cannot be null");
    }
}
