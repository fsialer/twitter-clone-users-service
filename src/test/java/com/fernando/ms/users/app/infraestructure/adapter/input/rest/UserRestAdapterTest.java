package com.fernando.ms.users.app.infraestructure.adapter.input.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.ms.users.app.application.ports.input.UserInputPort;
import com.fernando.ms.users.app.domain.models.User;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.UserRestAdapter;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.mapper.UserRestMapper;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.ChangePasswordRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.CreateUserRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.UpdateUserRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.UserAuthRequest;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response.ExistsUserResponse;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response.UserResponse;
import com.fernando.ms.users.app.utils.TestUtilUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@WebFluxTest(UserRestAdapter.class)
public class UserRestAdapterTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserInputPort userInputPort;

    @MockBean
    private UserRestMapper userRestMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("When Users Are Exists Expect Users Information Return Successfully")
    void When_UsersAreExists_Expect_UsersInformationReturnSuccessfully() {
        UserResponse userResponse = TestUtilUser.buildUserResponseMock();
        User user=TestUtilUser.buildUserMock();

        when(userInputPort.findAll()).thenReturn(Flux.just(user));
        when(userRestMapper.toUsersResponse(any(Flux.class))).thenReturn(Flux.just(userResponse));

        webTestClient.get()
                .uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].names").isEqualTo("Fernando Sialer")
                .jsonPath("$[0].email").isEqualTo("asialer05@hotmail.com");
        Mockito.verify(userInputPort,times(1)).findAll();
        Mockito.verify(userRestMapper,times(1)).toUsersResponse(any(Flux.class));
    }

    @Test
    @DisplayName("When UserIdentifier Is Correct Expect User Information Successfully")
    void When_UserIdentifierIsCorrect_Expect_UserInformationSuccessfully() {
        UserResponse userResponse = TestUtilUser.buildUserResponseMock();
        User user = TestUtilUser.buildUserMock();
     when(userInputPort.finById(anyLong())).thenReturn(Mono.just(user));
     when(userRestMapper.toUserResponse(any(Mono.class))).thenReturn(Mono.just(userResponse));
     webTestClient.get()
             .uri("/users/{id}",1L)
             .exchange()
             .expectStatus().isOk()
             .expectBody()
             .jsonPath("$.names").isEqualTo("Fernando Sialer")
             .jsonPath("$.email").isEqualTo("asialer05@hotmail.com");
        Mockito.verify(userInputPort,times(1)).finById(anyLong());
        Mockito.verify(userRestMapper,times(1)).toUserResponse(any(Mono.class));
    }


    @Test
    @DisplayName("When User Information Is Correct Expect User Information Saved Successfully")
    void When_UserInformationIsCorrect_Expect_UserInformationSavedSuccessfully() throws JsonProcessingException {
        CreateUserRequest createUserRequest = TestUtilUser.buildCreateUserRequestMock();
        User user = TestUtilUser.buildUserMock();
        UserResponse userResponse = TestUtilUser.buildUserResponseMock();

        when(userRestMapper.toUser(any(CreateUserRequest.class))).thenReturn(user);
        when(userInputPort.save(any(User.class))).thenReturn(Mono.just(user));
        when(userRestMapper.toUserResponse(any(User.class))).thenReturn(userResponse);

        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(createUserRequest))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.names").isEqualTo("Fernando Sialer")
                .jsonPath("$.email").isEqualTo("asialer05@hotmail.com");

        Mockito.verify(userRestMapper, times(1)).toUser(any(CreateUserRequest.class));
        Mockito.verify(userInputPort, times(1)).save(any(User.class));
        Mockito.verify(userRestMapper, times(1)).toUserResponse(any(User.class));
    }

    @Test
    @DisplayName("When User Information Is Correct Expect User Information Updated Successfully")
    void When_UserInformationIsCorrect_Expect_UserInformationUpdatedSuccessfully() throws JsonProcessingException {
        UpdateUserRequest updateUserRequest = TestUtilUser.buildUpdateUserRequestMock();
        User user = TestUtilUser.buildUserMock();
        UserResponse userResponse = TestUtilUser.buildUserResponseMock();

        when(userRestMapper.toUser(any(UpdateUserRequest.class))).thenReturn(user);
        when(userInputPort.save(any(User.class))).thenReturn(Mono.just(user));
        when(userRestMapper.toUserResponse(any(User.class))).thenReturn(userResponse);

        webTestClient.put()
                .uri("/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(updateUserRequest))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.names").isEqualTo("Fernando Sialer")
                .jsonPath("$.email").isEqualTo("asialer05@hotmail.com");

        Mockito.verify(userRestMapper, times(1)).toUser(any(UpdateUserRequest.class));
        Mockito.verify(userInputPort, times(1)).save(any(User.class));
        Mockito.verify(userRestMapper, times(1)).toUserResponse(any(User.class));
    }

    @Test
    @DisplayName("When User Exists Expect User Deleted Successfully")
    void When_UserExists_Expect_UserDeletedSuccessfully() {
        when(userInputPort.delete(anyLong())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/users/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent();

        Mockito.verify(userInputPort, times(1)).delete(anyLong());
    }

    @Test
    @DisplayName("When Password Is Correct Expect Password Changed Successfully")
    void When_PasswordIsCorrect_Expect_PasswordChangedSuccessfully() throws JsonProcessingException {
        ChangePasswordRequest changePasswordRequest = TestUtilUser.buildChangePasswordRequestMock();
        User user = TestUtilUser.buildUserMock();
        UserResponse userResponse = TestUtilUser.buildUserResponseMock();

        when(userRestMapper.toUser(any(ChangePasswordRequest.class))).thenReturn(user);
        when(userInputPort.changePassword(anyLong(), any(User.class))).thenReturn(Mono.just(user));
        when(userRestMapper.toUserResponse(any(User.class))).thenReturn(userResponse);

        webTestClient.put()
                .uri("/users/{id}/change-password", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(changePasswordRequest))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.names").isEqualTo("Fernando Sialer")
                .jsonPath("$.email").isEqualTo("asialer05@hotmail.com");

        Mockito.verify(userRestMapper, times(1)).toUser(any(ChangePasswordRequest.class));
        Mockito.verify(userInputPort, times(1)).changePassword(anyLong(), any(User.class));
        Mockito.verify(userRestMapper, times(1)).toUserResponse(any(User.class));
    }

    @Test
    @DisplayName("When Authentication Is Successful Expect User Returned")
    void When_AuthenticationIsSuccessful_Expect_UserReturned() throws JsonProcessingException {
        UserAuthRequest userAuthRequest = TestUtilUser.buildUserAuthRequestMock();
        User user = TestUtilUser.buildUserMock();
        UserResponse userResponse = TestUtilUser.buildUserResponseMock();

        when(userRestMapper.toUser(any(UserAuthRequest.class))).thenReturn(user);
        when(userInputPort.authentication(any(User.class))).thenReturn(Mono.just(user));
        when(userRestMapper.toUserResponse(any(User.class))).thenReturn(userResponse);

        webTestClient.post()
                .uri("/users/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(userAuthRequest))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.names").isEqualTo("Fernando Sialer")
                .jsonPath("$.email").isEqualTo("asialer05@hotmail.com");

        Mockito.verify(userRestMapper, times(1)).toUser(any(UserAuthRequest.class));
        Mockito.verify(userInputPort, times(1)).authentication(any(User.class));
        Mockito.verify(userRestMapper, times(1)).toUserResponse(any(User.class));
    }

    @Test
    @DisplayName("When User Verification Is Successful Expect User Verified")
    void When_UserVerificationIsSuccessful_Expect_UserVerified() {
        ExistsUserResponse existsUserResponse = TestUtilUser.buildExistsUserResponseMock();

        when(userInputPort.verifyUser(anyLong())).thenReturn(Mono.just(true));
        when(userRestMapper.toExistsUserResponse(anyBoolean())).thenReturn(existsUserResponse);

        webTestClient.get()
                .uri("/users/{id}/verify", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.exists").isEqualTo(true);

        Mockito.verify(userInputPort, times(1)).verifyUser(anyLong());
        Mockito.verify(userRestMapper, times(1)).toExistsUserResponse(anyBoolean());
    }

    @Test
    @DisplayName("When User Verification Is Incorrect Expect User Do Not Verified")
    void When_UserVerificationIsIncorrect_Expect_UserDoNotVerified() {
        ExistsUserResponse existsUserResponse = TestUtilUser.buildExistsUserResponseMock();
        existsUserResponse.setExists(false);
        when(userInputPort.verifyUser(anyLong())).thenReturn(Mono.just(false));
        when(userRestMapper.toExistsUserResponse(anyBoolean())).thenReturn(existsUserResponse);

        webTestClient.get()
                .uri("/users/{id}/verify", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.exists").isEqualTo(false);

        Mockito.verify(userInputPort, times(1)).verifyUser(anyLong());
        Mockito.verify(userRestMapper, times(1)).toExistsUserResponse(anyBoolean());
    }

    @Test
    @DisplayName("When User IDs Are Correct Expect Users Returned")
    void When_UserIDsAreCorrect_Expect_UsersReturned() {
        UserResponse userResponse1 = TestUtilUser.buildUserResponseMock();
        UserResponse userResponse2 = TestUtilUser.buildUserResponseMock();
        userResponse2.setId(2L);
        User user1 = TestUtilUser.buildUserMock();
        User user2 = TestUtilUser.buildUserMock();
        user2.setId(2L);

        when(userInputPort.findByIds(anyList())).thenReturn(Flux.just(user1, user2));
        when(userRestMapper.toUsersResponse(any(Flux.class))).thenReturn(Flux.just(userResponse1, userResponse2));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/users/find-by-ids")
                        .queryParam("ids", "1,2")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponse.class)
                .contains(userResponse1, userResponse2);

        Mockito.verify(userInputPort, times(1)).findByIds(anyList());
        Mockito.verify(userRestMapper, times(1)).toUsersResponse(any(Flux.class));
    }



}
