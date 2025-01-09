package com.fernando.ms.users.app.infraestructure.adapter.input.rest;

import com.fernando.ms.users.app.domain.exceptions.UserNotFoundException;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.GlobalControllerAdvice;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.UserRestAdapter;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response.ErrorResponse;
import com.fernando.ms.users.app.infrastructure.adapter.utils.ErrorCatalog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebFluxTest
@Import(GlobalControllerAdvice.class)
public class GlobalControllerAdviceTest {

    @MockBean
    private UserRestAdapter userRestAdapter;

    private WebTestClient webTestClient;


    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(userRestAdapter)
                .controllerAdvice(new GlobalControllerAdvice())
                .build();
    }

    @Test
    @DisplayName("Expect UserNotFoundException When User Identifier Is Invalid")
    void Expect_UserNotFoundException_When_UserIdentifierIsInvalid() {
        when(userRestAdapter.findById(anyLong())).thenReturn(Mono.error(new UserNotFoundException()));

        webTestClient.get()
                .uri("/users/1")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(ErrorCatalog.USER_NOT_FOUND.getCode());
                    assert response.getMessage().equals(ErrorCatalog.USER_NOT_FOUND.getMessage());
                });
    }

    @Test
    @DisplayName("Expect InternalServerError When Exception Occurs")
    void Expect_InternalServerError_When_ExceptionOccurs() {
        when(userRestAdapter.findById(anyLong())).thenReturn(Mono.error(new RuntimeException("Unexpected error")));

        webTestClient.get()
                .uri("/users/1")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(ErrorCatalog.INTERNAL_SERVER_ERROR.getCode());
                    assert response.getMessage().equals(ErrorCatalog.INTERNAL_SERVER_ERROR.getMessage());
                });
    }
}
