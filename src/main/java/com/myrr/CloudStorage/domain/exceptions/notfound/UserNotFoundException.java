package com.myrr.CloudStorage.domain.exceptions.notfound;

import com.myrr.CloudStorage.domain.enums.ApiErrorCode;

public class UserNotFoundException extends NotFoundApiException {
    public UserNotFoundException(Long id) {
        super(ApiErrorCode.USER_NOT_FOUND, "User with id %d is not found".formatted(id));
    }

    public UserNotFoundException(String username) {
        super(ApiErrorCode.USER_NOT_FOUND, "User with name %s is not found".formatted(username));
    }
}
