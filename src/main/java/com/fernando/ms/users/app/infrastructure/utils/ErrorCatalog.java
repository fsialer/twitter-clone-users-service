package com.fernando.ms.users.app.infrastructure.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCatalog {
    USER_NOT_FOUND("USER_MS_001", "User not found."),
    USER_BAD_PARAMETERS("USER_MS_002", "Invalid parameters for creation user"),
    USER_RULE_INVALID("USER_MS_003", "Rule invalid."),
    USER_EMAIL_USER_ALREADY_EXISTS("USER_MS_004", "Email already exists."),
    USER_FOLLOWER_NOT_FOUND("USER_MS_005", "User follower not found."),
    USER_FOLLOWED_NOT_FOUND("USER_MS_005", "User followed not found."),
    INTERNAL_SERVER_ERROR("USER_MS_000", "Internal server error.");


    private final String code;
    private final String message;
}
