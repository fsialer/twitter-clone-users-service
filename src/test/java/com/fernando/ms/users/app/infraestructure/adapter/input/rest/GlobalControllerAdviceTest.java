package com.fernando.ms.users.app.infraestructure.adapter.input.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.ms.users.app.application.ports.input.FollowInputPort;
import com.fernando.ms.users.app.application.ports.input.UserInputPort;
import com.fernando.ms.users.app.domain.exceptions.*;
import com.fernando.ms.users.app.domain.models.Follow;

import com.fernando.ms.users.app.infrastructure.adapter.input.rest.UserRestAdapter;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.mapper.FollowRestMapper;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.mapper.UserRestMapper;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.CreateFollowRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.CreateUserRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response.ErrorResponse;
import com.fernando.ms.users.app.infrastructure.utils.ErrorCatalog;
import com.fernando.ms.users.app.utils.TestUtilFollow;
import com.fernando.ms.users.app.utils.TestUtilUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@WebFluxTest({UserRestAdapter.class})
class GlobalControllerAdviceTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserInputPort userInputPort;

    @MockitoBean
    private UserRestMapper userRestMapper;

    @MockitoBean
    private FollowInputPort followInputPort;

    @MockitoBean
    private FollowRestMapper followRestMapper;


    @Test
    @DisplayName("Expect UserNotFoundException When User Identifier Is Invalid")
    void Expect_UserNotFoundException_When_UserIdentifierIsInvalid() {
        when(userInputPort.findById(anyString())).thenReturn(Mono.error(new UserNotFoundException()));
        webTestClient.get()
                .uri("/v1/users/{id}","cde8c071a420424abf28b189ae2cd698")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(ErrorCatalog.USER_NOT_FOUND.getCode());
                    assert response.getMessage().equals(ErrorCatalog.USER_NOT_FOUND.getMessage());
                });
        Mockito.verify(userInputPort, times(1)).findById(anyString());
        Mockito.verify(userRestMapper,never()).toUserResponse(any());
    }

    @Test
    @DisplayName("Expect InternalServerError When Exception Occurs")
    void Expect_InternalServerError_When_ExceptionOccurs() {
        when(userInputPort.findById(anyString())).thenReturn(Mono.error(new RuntimeException("Unexpected error")));

        webTestClient.get()
                .uri("/v1/users/{id}/error","cde8c071a420424abf28b189ae2cd698")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(ErrorCatalog.INTERNAL_SERVER_ERROR.getCode());
                    assert response.getMessage().equals(ErrorCatalog.INTERNAL_SERVER_ERROR.getMessage());
                });
        Mockito.verify(userInputPort, never()).findById(anyString());
    }

    @Test
    @DisplayName("Expect UserEmailAlreadyExistsException When Email Already Exists")
    void Expect_UserEmailAlreadyExistsException_When_EmailAlreadyExists() throws JsonProcessingException {
        CreateUserRequest createUserRequest = TestUtilUser.buildCreateUserRequestMock();
        when(userRestMapper.toUser(any(CreateUserRequest.class))).thenReturn(TestUtilUser.buildUserMock());
        when(userInputPort.save(any())).thenReturn(Mono.error(new UserEmailAlreadyExistsException(createUserRequest.getEmail())));

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
        Mockito.verify(userRestMapper, times(1)).toUser(any(CreateUserRequest.class));
        Mockito.verify(userInputPort, times(1)).save(any());
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
        Mockito.verify(userRestMapper, never()).toUser(any(CreateUserRequest.class));
        Mockito.verify(userInputPort, never()).save(any());
    }



    @Test
    @DisplayName("Expect FollowerNotFoundException When header X-User-Id Is Invalid")
    void Expect_FollowerNotFoundException_When_Header_XUserId_Is_Invalid() {
        CreateFollowRequest createFollowRequest = TestUtilFollow.buildCreateFollowRequestMock();
        Follow follow=TestUtilFollow.buildFollowMock();
        when(followRestMapper.toFollow(anyString(), any(CreateFollowRequest.class)))
                .thenReturn(follow);

        when(followInputPort.followUser(any())).thenReturn(Mono.error(FollowerNotFoundException::new));

        webTestClient.post()
                .uri("/v1/users/follow")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-User-Id", "cde8c071a420424abf28b189ae2cd698")
                .bodyValue(createFollowRequest)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(ErrorCatalog.USER_FOLLOWER_NOT_FOUND.getCode());
                    assert response.getMessage().equals(ErrorCatalog.USER_FOLLOWER_NOT_FOUND.getMessage());
                });

        Mockito.verify(followRestMapper, times(1)).toFollow(anyString(), any(CreateFollowRequest.class));
        Mockito.verify(followInputPort, times(1)).followUser(any());
    }

    @Test
    @DisplayName("Expect FollowedNotFoundException When Identifier FollowedId Is Invalid")
    void Expect_FollowedNotFoundException_When_IdentifierFollowedIdIsInvalid() {
        CreateFollowRequest createFollowRequest = TestUtilFollow.buildCreateFollowRequestMock();

        when(followRestMapper.toFollow(anyString(), any(CreateFollowRequest.class)))
                .thenReturn(TestUtilFollow.buildFollowMock());
        when(followInputPort.followUser(any(Follow.class))).thenReturn(Mono.error(FollowedNotFoundException::new));

        webTestClient.post()
                .uri("/v1/users/follow")
                .header("X-User-Id", "68045526dffe6e2de223e55b")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createFollowRequest)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(ErrorCatalog.USER_FOLLOWED_NOT_FOUND.getCode());
                    assert response.getMessage().equals(ErrorCatalog.USER_FOLLOWED_NOT_FOUND.getMessage());
                });

        Mockito.verify(followRestMapper, times(1)).toFollow(anyString(), any(CreateFollowRequest.class));
        Mockito.verify(followInputPort, times(1)).followUser(any(Follow.class));
    }

    @Test
    @DisplayName("Expect UserRuleException When Identifier Follower And FollowedId Is Invalid")
    void Expect_UserRuleException_When_IdentifierFollowerAndFollowedIsInvalid() {
        CreateFollowRequest createFollowRequest = TestUtilFollow.buildCreateFollowRequestMock();

        when(followRestMapper.toFollow(anyString(), any(CreateFollowRequest.class)))
                .thenReturn(TestUtilFollow.buildFollowMock());
        when(followInputPort.followUser(any())).thenReturn(Mono.error(new UserRuleException("User cannot follow themselves!")));

        webTestClient.post()
                .uri("/v1/users/follow")
                .header("X-User-Id", "68045526dffe6e2de223e55b")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createFollowRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(ErrorCatalog.USER_RULE_INVALID.getCode());
                    assert response.getMessage().equals(ErrorCatalog.USER_RULE_INVALID.getMessage());
                });

        Mockito.verify(followRestMapper, times(1)).toFollow(anyString(), any(CreateFollowRequest.class));
        Mockito.verify(followInputPort, times(1)).followUser(any());
    }
}
