package com.fernando.ms.users.app.infrastructure.adapter.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCatalog {
    USER_NOT_FOUND("USER_MS_001", "User not found."),
    USER_BAD_PARAMETERS("USER_MS_002", "Invalid parameters for creation user"),
    INTERNAL_SERVER_ERROR("USER_MS_000", "Internal server error.");


    private final String code;
    private final String message;
}
