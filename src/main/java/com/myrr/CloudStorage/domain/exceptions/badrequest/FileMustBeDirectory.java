package com.myrr.CloudStorage.domain.exceptions.badrequest;

import com.myrr.CloudStorage.domain.enums.ApiErrorCode;

import java.util.UUID;

public class FileMustBeDirectory extends BadRequestException {
    public FileMustBeDirectory(UUID id) {
        super(ApiErrorCode.FILE_MUST_BE_DIRECTORY, "File with id %s must be directory".formatted(id));
    }
}
