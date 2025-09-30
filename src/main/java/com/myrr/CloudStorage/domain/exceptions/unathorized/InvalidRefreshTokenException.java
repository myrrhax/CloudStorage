package com.myrr.CloudStorage.domain.exceptions.unathorized;

import com.myrr.CloudStorage.domain.enums.ApiErrorCode;

public class InvalidRefreshTokenException extends UnauthorizedApiException {
    public InvalidRefreshTokenException() {
        super(ApiErrorCode.INVALID_REFRESH_TOKEN, "Unable to refresh tokens");
    }
}
