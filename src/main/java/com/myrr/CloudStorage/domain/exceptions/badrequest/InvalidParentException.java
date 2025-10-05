package com.myrr.CloudStorage.domain.exceptions.badrequest;

import com.myrr.CloudStorage.domain.enums.ApiErrorCode;

public class InvalidParentException extends BadRequestException {
    public InvalidParentException() {
        super(ApiErrorCode.INVALID_PARENT, "Parent must be directory");
    }
}
