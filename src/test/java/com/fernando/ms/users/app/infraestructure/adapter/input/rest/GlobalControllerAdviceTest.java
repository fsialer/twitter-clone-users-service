package com.fernando.ms.users.app.infraestructure.adapter.input.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.ms.users.app.domain.exceptions.*;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.GlobalControllerAdvice;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.UserRestAdapter;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.mapper.UserRestMapper;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.ChangePasswordRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.CreateUserRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response.ErrorResponse;
import com.fernando.ms.users.app.infrastructure.adapter.utils.ErrorCatalog;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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


    @Test
    @DisplayName("Expect UserUsernameAlreadyExistsException When Username Already Exists")
    void Expect_UserUsernameAlreadyExistsException_When_UsernameAlreadyExists() throws JsonProcessingException {
        CreateUserRequest createUserRequest = TestUtilUser.buildCreateUserRequestMock();
        when(userRestMapper.toUser(any(CreateUserRequest.class))).thenReturn(TestUtilUser.buildUserMock());
        when(userRestAdapter.save(any())).thenReturn(Mono.error(new UserUsernameAlreadyExistsException(createUserRequest.getUsername())));

        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(createUserRequest)) // Replace with actual request body
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(ErrorCatalog.USER_USERNAME_ALREADY_EXISTS.getCode());
                    assert response.getMessage().equals(ErrorCatalog.USER_USERNAME_ALREADY_EXISTS.getMessage());
                    assert response.getDetails().contains("User username: " + createUserRequest.getUsername() + " already exists!");
                });
    }

    @Test
    @DisplayName("Expect UserEmailAlreadyExistsException When Email Already Exists")
    void Expect_UserEmailAlreadyExistsException_When_EmailAlreadyExists() throws JsonProcessingException {
        CreateUserRequest createUserRequest = TestUtilUser.buildCreateUserRequestMock();
        when(userRestMapper.toUser(any(CreateUserRequest.class))).thenReturn(TestUtilUser.buildUserMock());
        when(userRestAdapter.save(any())).thenReturn(Mono.error(new UserEmailAlreadyExistsException(createUserRequest.getEmail())));

        webTestClient.post()
                .uri("/users")
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
                .uri("/users")
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

    @Test
    @DisplayName("Expect CredentialFailedException When Credentials Fail")
    void Expect_CredentialFailedException_When_CredentialsFail() throws JsonProcessingException {
        when(userRestAdapter.changePassword(anyLong(), any())).thenReturn(Mono.error(new CredentialFailedException()));

        webTestClient.put()
                .uri("/users/{id}/change-password", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(TestUtilUser.buildChangePasswordRequestMock()))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(ErrorCatalog.USER_CREDENTIAL_FAIL.getCode());
                    assert response.getMessage().equals(ErrorCatalog.USER_CREDENTIAL_FAIL.getMessage());
                });
    }

    @Test
    @DisplayName("Expect PasswordNotConfirmException When Passwords Do Not Match")
    void Expect_PasswordNotConfirmException_When_PasswordsDoNotMatch() throws JsonProcessingException {
        ChangePasswordRequest rq= TestUtilUser.buildChangePasswordRequestMock();
        rq.setConfirmPassword("124");
        when(userRestAdapter.changePassword(anyLong(), any())).thenReturn(Mono.error(new PasswordNotConfirmException()));

        webTestClient.put()
                .uri("/users/{id}/change-password", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(rq))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(ErrorCatalog.USER_PASSWORD_NO_CONFIRM.getCode());
                    assert response.getMessage().equals(ErrorCatalog.USER_PASSWORD_NO_CONFIRM.getMessage());
                });
    }
}
