package com.myrr.CloudStorage.domain.exceptions.notfound;

import com.myrr.CloudStorage.domain.enums.ApiErrorCode;

public class FilesNotFoundException extends NotFoundApiException {
    public FilesNotFoundException() {
        super(ApiErrorCode.NO_FILES, "Files with given filters are not found");
    }
}
