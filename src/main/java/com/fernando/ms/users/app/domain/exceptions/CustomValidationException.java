package com.fernando.ms.users.app.domain.exceptions;

import lombok.Getter;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Getter
public class CustomValidationException extends RuntimeException{
    private final MethodArgumentNotValidException exception;

    public CustomValidationException(MethodArgumentNotValidException exception) {
        super("Validation failed");
        this.exception = exception;
    }

}
