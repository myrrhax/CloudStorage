package com.myrr.CloudStorage.domain.exceptions.conflict;

import com.myrr.CloudStorage.domain.enums.ApiErrorCode;

public class FileAlreadyExistsException extends ConflictApiException {
    public FileAlreadyExistsException() {
        super(ApiErrorCode.FILE_CONFLICT, "File already exists");
    }
}
