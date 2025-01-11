package com.fernando.ms.users.app.infrastructure.adapter.input.rest;

import com.fernando.ms.users.app.domain.exceptions.*;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Collections;

import static com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.enums.ErrorType.FUNCTIONAL;
import static com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.enums.ErrorType.SYSTEM;
import static com.fernando.ms.users.app.infrastructure.adapter.utils.ErrorCatalog.*;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public Mono<ErrorResponse> handleUserNotFoundException() {
        return Mono.just(ErrorResponse.builder()
                .code(USER_NOT_FOUND.getCode())
                .type(FUNCTIONAL)
                .message(USER_NOT_FOUND.getMessage())
                .timestamp(LocalDate.now().toString())
                .build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ErrorResponse> handleWebExchangeBindException(
            WebExchangeBindException e) {
        BindingResult bindingResult = e.getBindingResult();
        return Mono.just(ErrorResponse.builder()
                .code(USER_BAD_PARAMETERS.getCode())
                .type(FUNCTIONAL)
                .message(USER_BAD_PARAMETERS.getMessage())
                .details(bindingResult.getFieldErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList())
                .timestamp(LocalDate.now().toString())
                .build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserUsernameAlreadyExistsException.class)
    public  Mono<ErrorResponse> handleUserUsernameAlreadyExistsException(UserUsernameAlreadyExistsException e) {
        return Mono.just(ErrorResponse.builder()
                .code(USER_USERNAME_ALREADY_EXISTS.getCode())
                .type(FUNCTIONAL)
                .message(USER_USERNAME_ALREADY_EXISTS.getMessage())
                .timestamp(LocalDate.now().toString())
                .details(Collections.singletonList(e.getMessage()))
                .build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserEmailAlreadyExistsException.class)
    public  Mono<ErrorResponse> handleUserEmailUserAlreadyExistsException(UserEmailAlreadyExistsException e) {
        return Mono.just(ErrorResponse.builder()
                .code(USER_EMAIL_USER_ALREADY_EXISTS.getCode())
                .type(FUNCTIONAL)
                .message(USER_EMAIL_USER_ALREADY_EXISTS.getMessage())
                .timestamp(LocalDate.now().toString())
                .details(Collections.singletonList(e.getMessage()))
                .build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CredentialFailedException.class)
    public  Mono<ErrorResponse> handleCredentialFailedException(CredentialFailedException e) {
        return Mono.just(ErrorResponse.builder()
                .code(USER_CREDENTIAL_FAIL.getCode())
                .type(FUNCTIONAL)
                .message(USER_CREDENTIAL_FAIL.getMessage())
                .timestamp(LocalDate.now().toString())
                //.details(Collections.singletonList(e.getMessage()))
                .build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PasswordNotConfirmException.class)
    public  Mono<ErrorResponse> handlePasswordNotConfirmException(PasswordNotConfirmException e) {
        return Mono.just(ErrorResponse.builder()
                .code(USER_PASSWORD_NO_CONFIRM.getCode())
                .type(FUNCTIONAL)
                .message(USER_PASSWORD_NO_CONFIRM.getMessage())
                .timestamp(LocalDate.now().toString())
                //.details(Collections.singletonList(e.getMessage()))
                .build());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Mono<ErrorResponse> handleException(Exception e) {

        return Mono.just(ErrorResponse.builder()
                .code(INTERNAL_SERVER_ERROR.getCode())
                .type(SYSTEM)
                .message(INTERNAL_SERVER_ERROR.getMessage())
                .details(Collections.singletonList(e.getMessage()))
                .timestamp(LocalDate.now().toString())
                .build());
    }
}
