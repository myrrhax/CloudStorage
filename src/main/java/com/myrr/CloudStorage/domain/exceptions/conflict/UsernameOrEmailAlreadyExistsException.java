package com.myrr.CloudStorage.domain.exceptions.conflict;

import com.myrr.CloudStorage.domain.enums.ApiErrorCode;

public class UsernameOrEmailAlreadyExistsException extends ConflictApiException {
    public UsernameOrEmailAlreadyExistsException(String username,
                                                 String email) {
        super(ApiErrorCode.USERNAME_OR_EMAIL_ALREADY_EXISTS,
                "User with username %s or email %s already exists".formatted(username, email));
    }
}
