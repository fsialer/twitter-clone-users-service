package com.fernando.ms.users.app.infraestructure.adapter.input.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.ms.users.app.application.ports.input.FollowInputPort;
import com.fernando.ms.users.app.application.ports.input.UserInputPort;
import com.fernando.ms.users.app.domain.models.Follow;
import com.fernando.ms.users.app.domain.models.User;

import com.fernando.ms.users.app.infrastructure.adapter.input.rest.UserRestAdapter;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.mapper.FollowRestMapper;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.mapper.UserRestMapper;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.CreateFollowRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.CreateUserRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.UpdateUserRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response.ExistsUserResponse;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response.UserResponse;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@WebFluxTest({UserRestAdapter.class})
class UserRestAdapterTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserInputPort userInputPort;

    @MockitoBean
    private UserRestMapper userRestMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FollowRestMapper followRestMapper;

    @MockitoBean
    private FollowInputPort followInputPort;



    @Test
    @DisplayName("When Users Are Exists Expect Users Information Return Successfully")
    void When_UsersAreExists_Expect_UsersInformationReturnSuccessfully() {
        when(userInputPort.findAll()).thenReturn(Flux.just(TestUtilUser.buildUserMock()));
        when(userRestMapper.toUsersResponse(any(Flux.class))).thenReturn(Flux.just(TestUtilUser.buildUserResponseMock()));

        webTestClient.get()
                .uri("/v1/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].names").isEqualTo("Fernando")
                .jsonPath("$[0].email").isEqualTo("asialer05@hotmail.com");
        Mockito.verify(userInputPort,times(1)).findAll();
        Mockito.verify(userRestMapper,times(1)).toUsersResponse(any(Flux.class));
    }

    @Test
    @DisplayName("When UserIdentifier Is Correct Expect User Information Successfully")
    void When_UserIdentifierIsCorrect_Expect_UserInformationSuccessfully() {
     when(userInputPort.findById(anyString())).thenReturn(Mono.just(TestUtilUser.buildUserMock()));
     when(userRestMapper.toUserResponse(any(User.class))).thenReturn(TestUtilUser.buildUserResponseMock());
     webTestClient.get()
             .uri("/v1/users/{id}","1L")
             .exchange()
             .expectStatus().isOk()
             .expectBody()
             .jsonPath("$.names").isEqualTo("Fernando")
             .jsonPath("$.email").isEqualTo("asialer05@hotmail.com");
        Mockito.verify(userInputPort,times(1)).findById(anyString());
        Mockito.verify(userRestMapper,times(1)).toUserResponse(any(User.class));
    }


    @Test
    @DisplayName("When User Information Is Correct Expect User Information Saved Successfully")
    void When_UserInformationIsCorrect_Expect_UserInformationSavedSuccessfully() throws JsonProcessingException {

        User user=TestUtilUser.buildUserMock();
        when(userRestMapper.toUser(any(CreateUserRequest.class))).thenReturn(user);
        when(userInputPort.save(any(User.class))).thenReturn(Mono.just(user));
        when(userRestMapper.toUserResponse(any(User.class))).thenReturn( TestUtilUser.buildUserResponseMock());

        webTestClient.post()
                .uri("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString( TestUtilUser.buildCreateUserRequestMock()))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.names").isEqualTo("Fernando")
                .jsonPath("$.email").isEqualTo("asialer05@hotmail.com");

        Mockito.verify(userRestMapper, times(1)).toUser(any(CreateUserRequest.class));
        Mockito.verify(userInputPort, times(1)).save(any(User.class));
        Mockito.verify(userRestMapper, times(1)).toUserResponse(any(User.class));
    }


    @Test
    @DisplayName("When User Information Is Correct Expect User Information Updated Successfully")
    void When_UserInformationIsCorrect_Expect_UserInformationUpdatedSuccessfully() throws JsonProcessingException {
        when(userRestMapper.toUser(any(UpdateUserRequest.class))).thenReturn( TestUtilUser.buildUserMock());
        when(userInputPort.update(anyString(),any(User.class))).thenReturn(Mono.just( TestUtilUser.buildUserMock()));
        when(userRestMapper.toUserResponse(any(User.class))).thenReturn( TestUtilUser.buildUserResponseMock());

        webTestClient.put()
                .uri("/v1/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString( TestUtilUser.buildUpdateUserRequestMock()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.names").isEqualTo("Fernando")
                .jsonPath("$.email").isEqualTo("asialer05@hotmail.com");

        Mockito.verify(userRestMapper, times(1)).toUser(any(UpdateUserRequest.class));
        Mockito.verify(userInputPort, times(1)).update(anyString(),any(User.class));
        Mockito.verify(userRestMapper, times(1)).toUserResponse(any(User.class));
    }

    @Test
    @DisplayName("When User Exists Expect User Deleted Successfully")
    void When_UserExists_Expect_UserDeletedSuccessfully() {
        when(userInputPort.delete(anyString())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/v1/users/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent();

        Mockito.verify(userInputPort, times(1)).delete(anyString());
    }


    @Test
    @DisplayName("When User Verification Is Successful Expect User Verified")
    void When_UserVerificationIsSuccessful_Expect_UserVerified() {
        when(userInputPort.verifyUser(anyString())).thenReturn(Mono.just(true));
        when(userRestMapper.toExistsUserResponse(anyBoolean())).thenReturn(TestUtilUser.buildExistsUserResponseMock());

        webTestClient.get()
                .uri("/v1/users/{id}/verify", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.exists").isEqualTo(true);

        Mockito.verify(userInputPort, times(1)).verifyUser(anyString());
        Mockito.verify(userRestMapper, times(1)).toExistsUserResponse(anyBoolean());
    }

    @Test
    @DisplayName("When User Verification Is Incorrect Expect User Do Not Verified")
    void When_UserVerificationIsIncorrect_Expect_UserDoNotVerified() {
        ExistsUserResponse existsUserResponse = TestUtilUser.buildExistsUserResponseMock();
        existsUserResponse.setExists(false);
        when(userInputPort.verifyUser(anyString())).thenReturn(Mono.just(false));
        when(userRestMapper.toExistsUserResponse(anyBoolean())).thenReturn(existsUserResponse);

        webTestClient.get()
                .uri("/v1/users/{id}/verify", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.exists").isEqualTo(false);

        Mockito.verify(userInputPort, times(1)).verifyUser(anyString());
        Mockito.verify(userRestMapper, times(1)).toExistsUserResponse(anyBoolean());
    }

    @Test
    @DisplayName("When User IDs Are Correct Expect Users Returned")
    void When_UserIDsAreCorrect_Expect_UsersReturned() {
        UserResponse userResponse2 = TestUtilUser.buildUserResponseMock();
        userResponse2.setId("cde8c071a420424abf28b189ae2cd6982");
        User user2 = TestUtilUser.buildUserMock();
        user2.setId("cde8c071a420424abf28b189ae2cd69824");

        when(userInputPort.findByIds(anyList())).thenReturn(Flux.just(TestUtilUser.buildUserMock(), user2));
        when(userRestMapper.toUsersResponse(any(Flux.class))).thenReturn(Flux.just( TestUtilUser.buildUserResponseMock(), userResponse2));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/v1/users/find-by-ids")
                        .queryParam("ids", "1,2")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponse.class)
                .contains( TestUtilUser.buildUserResponseMock(), userResponse2);

        Mockito.verify(userInputPort, times(1)).findByIds(anyList());
        Mockito.verify(userRestMapper, times(1)).toUsersResponse(any(Flux.class));
    }

    @Test
    @DisplayName("When ValidFollowRequest Expect User Followed Successfully")
    void When_ValidFollowRequest_Expect_UserFollowedSuccessfully() {
        CreateFollowRequest createFollowRequest = TestUtilFollow.buildCreateFollowRequestMock();
        Follow follow=TestUtilFollow.buildFollowMock();
        when(followRestMapper.toFollow(anyString(), any(CreateFollowRequest.class)))
                .thenReturn(follow);

        when(followInputPort.followUser(any(Follow.class))).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/v1/users/follow")
                .header("X-User-Id", "68045526dffe6e2de223e55b")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createFollowRequest)
                .exchange()
                .expectStatus().isNoContent();

        Mockito.verify(followRestMapper, times(1)).toFollow(anyString(), any(CreateFollowRequest.class));
        Mockito.verify(followInputPort, times(1)).followUser(any(Follow.class));
    }

    @Test
    @DisplayName("When ValidFollowId Expect UnfollowSuccessfully")
    void When_ValidFollowId_Expect_UnfollowSuccessfully() {
        when(followInputPort.unFollowUser(anyString(), anyString())).thenReturn(Mono.empty());
        webTestClient.delete()
                .uri("/v1/users/unfollow/{id}", "followId123")
                .header("X-User-Id", "userId123")
                .exchange()
                .expectStatus().isNoContent();

        verify(followInputPort, times(1)).unFollowUser("followId123", "userId123");
    }

    @Test
    @DisplayName("When UserAuthenticate Is Correct Expect User Information Successfully")
    void When_UserAuthenticateIsCorrect_Expect_UserInformationSuccessfully() {
        when(userInputPort.findByUserId(anyString())).thenReturn(Mono.just(TestUtilUser.buildUserMock()));
        when(userRestMapper.toUserResponse(any(User.class))).thenReturn(TestUtilUser.buildUserResponseMock());
        webTestClient.get()
                .uri("/v1/users/me")
                .header("X-User-Id", "4f57f5d4f668d4ff5")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.names").isEqualTo("Fernando")
                .jsonPath("$.email").isEqualTo("asialer05@hotmail.com");
        Mockito.verify(userInputPort,times(1)).findByUserId(anyString());
        Mockito.verify(userRestMapper,times(1)).toUserResponse(any(User.class));
    }

    @Test
    @DisplayName("When User Authenticated Is Correct Expect User Information Updated Successfully")
    void When_UserAuthenticatedIsCorrect_Expect_UserInformationUpdatedSuccessfully() throws JsonProcessingException {
        when(userRestMapper.toUser(any(UpdateUserRequest.class))).thenReturn( TestUtilUser.buildUserMock());
        when(userInputPort.updateByUserId(anyString(),any(User.class))).thenReturn(Mono.just( TestUtilUser.buildUserMock()));
        when(userRestMapper.toUserResponse(any(User.class))).thenReturn( TestUtilUser.buildUserResponseMock());

        webTestClient.put()
                .uri("/v1/users/me")
                .header("X-User-Id", "4f57f5d4f668d4ff5")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString( TestUtilUser.buildUpdateUserRequestMock()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.names").isEqualTo("Fernando")
                .jsonPath("$.email").isEqualTo("asialer05@hotmail.com");

        Mockito.verify(userRestMapper, times(1)).toUser(any(UpdateUserRequest.class));
        Mockito.verify(userInputPort, times(1)).updateByUserId(anyString(),any(User.class));
        Mockito.verify(userRestMapper, times(1)).toUserResponse(any(User.class));
    }

    @Test
    @DisplayName("When UserId Is Correct Expect List User Correct")
    void When_UserIdIsCorrect_Expect_ListUserCorrect() {
        User user=TestUtilUser.buildUserMock();
        UserResponse userResponse=TestUtilUser.buildUserResponseMock();
        when(userRestMapper.toUsersResponse(any(Flux.class))).thenReturn(Flux.just(userResponse));
        when(userInputPort.findUserFollowed(anyString())).thenReturn(Flux.just( user));
        webTestClient.get()
                .uri("/v1/users/{userId}/followed","4f57f5d4f668d4ff5")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo("cde8c071a420424abf28b189ae2cd6982")
                .jsonPath("$[0].names").isEqualTo("Fernando")
                .jsonPath("$[0].lastNames").isEqualTo("Sialer Ayala");

        Mockito.verify(userRestMapper, times(1)).toUsersResponse(any(Flux.class));
        Mockito.verify(userInputPort, times(1)).findUserFollowed(anyString());
    }

    @Test
    @DisplayName("When User UserId Is Correct Expect User Information Successfully")
    void When_UserUserIdIsCorrect_Expect_UserInformationSuccessfully() {
        when(userInputPort.findByUserId(anyString())).thenReturn(Mono.just(TestUtilUser.buildUserMock()));
        when(userRestMapper.toUserResponse(any(User.class))).thenReturn(TestUtilUser.buildUserResponseMock());
        webTestClient.get()
                .uri("/v1/users/{userId}/user-id","4f57f5d4f668d4ff5")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.names").isEqualTo("Fernando")
                .jsonPath("$.email").isEqualTo("asialer05@hotmail.com");
        Mockito.verify(userInputPort,times(1)).findByUserId(anyString());
        Mockito.verify(userRestMapper,times(1)).toUserResponse(any(User.class));
    }

    @Test
    @DisplayName("When FullName Exists Expect Users Information Return Successfully")
    void When_FullNameExists_Expect_UsersInformationReturnSuccessfully() {
        when(userInputPort.findUserByFullName(anyString(),anyInt(),anyInt())).thenReturn(Flux.just(TestUtilUser.buildUserMock()));
        when(userRestMapper.toUsersResponse(any(Flux.class))).thenReturn(Flux.just(TestUtilUser.buildUserResponseMock()));

        webTestClient.get()
                .uri(uriBuilder ->
                    uriBuilder.path("/v1/users/search")
                            .queryParam("full_name","fer")
                            .queryParam("page",1)
                            .queryParam("size",20)
                            .build()
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].names").isEqualTo("Fernando")
                .jsonPath("$[0].lastNames").isEqualTo("Sialer Ayala")
                .jsonPath("$[0].fullName").isEqualTo("Fernando Sialer Ayala")
                .jsonPath("$[0].email").isEqualTo("asialer05@hotmail.com");
        Mockito.verify(userInputPort,times(1)).findUserByFullName(anyString(),anyInt(),anyInt());
        Mockito.verify(userRestMapper,times(1)).toUsersResponse(any(Flux.class));
    }
}
