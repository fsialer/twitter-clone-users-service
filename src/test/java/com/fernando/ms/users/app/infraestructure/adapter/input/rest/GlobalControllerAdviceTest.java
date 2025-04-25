package com.fernando.ms.users.app.infraestructure.adapter.input.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.ms.users.app.domain.exceptions.*;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.GlobalControllerAdvice;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.UserRestAdapter;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.mapper.UserRestMapper;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.CreateUserRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response.ErrorResponse;
import com.fernando.ms.users.app.infrastructure.utils.ErrorCatalog;
import com.fernando.ms.users.app.utils.TestUtilUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebFluxTest
@Import(GlobalControllerAdvice.class)
class GlobalControllerAdviceTest {

    @MockitoBean
    private UserRestAdapter userRestAdapter;

    @MockitoBean
    private UserRestMapper userRestMapper;

    @Autowired
    private ObjectMapper objectMapper;

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
        when(userRestAdapter.findById(anyString())).thenReturn(Mono.error(new UserNotFoundException()));

        webTestClient.get()
                .uri("/v1/users/cde8c071a420424abf28b189ae2cd698")
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
        when(userRestAdapter.findById(anyString())).thenReturn(Mono.error(new RuntimeException("Unexpected error")));

        webTestClient.get()
                .uri("/v1/users/1")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(ErrorCatalog.INTERNAL_SERVER_ERROR.getCode());
                    assert response.getMessage().equals(ErrorCatalog.INTERNAL_SERVER_ERROR.getMessage());
                });
    }

    @Test
    @DisplayName("Expect UserEmailAlreadyExistsException When Email Already Exists")
    void Expect_UserEmailAlreadyExistsException_When_EmailAlreadyExists() throws JsonProcessingException {
        CreateUserRequest createUserRequest = TestUtilUser.buildCreateUserRequestMock();
        when(userRestMapper.toUser(any(CreateUserRequest.class))).thenReturn(TestUtilUser.buildUserMock());
        when(userRestAdapter.save(any())).thenReturn(Mono.error(new UserEmailAlreadyExistsException(createUserRequest.getEmail())));

        webTestClient.post()
                .uri("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(createUserRequest)) // Replace with actual request body
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(ErrorCatalog.USER_EMAIL_USER_ALREADY_EXISTS.getCode());
                    assert response.getMessage().equals(ErrorCatalog.USER_EMAIL_USER_ALREADY_EXISTS.getMessage());
                    assert response.getDetails().contains("User email: "+createUserRequest.getEmail()+" already exists!");
                });
    }


    @Test
    @DisplayName("Expect WebExchangeBindException When User Information Is Invalid")
    void Expect_WebExchangeBindException_When_UserInformationIsInvalid() throws JsonProcessingException {
        CreateUserRequest rq=TestUtilUser.buildCreateUserRequestMock();
        rq.setEmail("");
        webTestClient.post()
                .uri("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(rq))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(ErrorCatalog.USER_BAD_PARAMETERS.getCode());
                    assert response.getMessage().equals(ErrorCatalog.USER_BAD_PARAMETERS.getMessage());
                });
    }

}
