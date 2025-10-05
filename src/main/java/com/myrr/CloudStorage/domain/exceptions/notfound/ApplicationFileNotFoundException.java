package com.myrr.CloudStorage.domain.exceptions.notfound;

import com.myrr.CloudStorage.domain.enums.ApiErrorCode;

import java.util.UUID;

public class ApplicationFileNotFoundException extends NotFoundApiException {
    public ApplicationFileNotFoundException(UUID fileId) {
        super(ApiErrorCode.FILE_NOT_FOUND, "File with id: %s is not found".formatted(fileId));
    }
}
