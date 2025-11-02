package com.myrr.CloudStorage.domain.exceptions.badrequest;

import com.myrr.CloudStorage.domain.enums.ApiErrorCode;

public class FileExtensionCantBeChangedException extends BadRequestException {
    public FileExtensionCantBeChangedException(String originalExtension, String newExtension) {
        super(ApiErrorCode.FILE_EXTENSION_CANT_BE_CHANGED, "File extension can't be changed from %s to %s"
                .formatted(originalExtension, newExtension));
    }
}
