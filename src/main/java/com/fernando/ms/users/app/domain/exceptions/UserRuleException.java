package com.fernando.ms.users.app.domain.exceptions;

public class UserRuleException extends RuntimeException{
    public UserRuleException(String msg) {
        super(msg);
    }
}
