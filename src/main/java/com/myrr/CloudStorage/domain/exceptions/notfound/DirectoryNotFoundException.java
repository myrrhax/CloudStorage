package com.myrr.CloudStorage.domain.exceptions.notfound;

import com.myrr.CloudStorage.domain.enums.ApiErrorCode;

import java.util.UUID;

public class DirectoryNotFoundException extends NotFoundApiException {
    public DirectoryNotFoundException(UUID id) {
        super(ApiErrorCode.DIRECTORY_NOT_FOUND, "Directory with id: %s is not found".formatted(id.toString()));
    }
}
