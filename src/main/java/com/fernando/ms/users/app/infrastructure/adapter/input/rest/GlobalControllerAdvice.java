package com.fernando.ms.users.app.infrastructure.adapter.input.rest;

import com.fernando.ms.users.app.domain.exceptions.UserNotFoundException;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Collections;

import static com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.enums.ErrorType.FUNCTIONAL;
import static com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.enums.ErrorType.SYSTEM;
import static com.fernando.ms.users.app.infrastructure.adapter.utils.ErrorCatalog.INTERNAL_SERVER_ERROR;
import static com.fernando.ms.users.app.infrastructure.adapter.utils.ErrorCatalog.USER_NOT_FOUND;

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
