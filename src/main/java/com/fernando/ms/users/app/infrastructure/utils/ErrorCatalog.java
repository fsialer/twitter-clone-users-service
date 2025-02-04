package com.fernando.ms.users.app.infrastructure.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCatalog {
    USER_NOT_FOUND("USER_MS_001", "User not found."),
    USER_BAD_PARAMETERS("USER_MS_002", "Invalid parameters for creation user"),
    USER_USERNAME_ALREADY_EXISTS("USER_MS_003", "Username already exists."),
    USER_EMAIL_USER_ALREADY_EXISTS("USER_MS_004", "Email already exists."),
    USER_CREDENTIAL_FAIL("USER_MS_005", "Password not valid."),
    USER_PASSWORD_NO_CONFIRM("USER_MS_006", "New password not equal to confirm."),
    INTERNAL_SERVER_ERROR("USER_MS_000", "Internal server error.");


    private final String code;
    private final String message;
}
