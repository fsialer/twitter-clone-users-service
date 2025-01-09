package com.fernando.ms.users.app.infraestructure.adapter.input.rest;

import com.fernando.ms.users.app.application.ports.input.UserInputPort;
import com.fernando.ms.users.app.domain.models.User;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.UserRestAdapter;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.mapper.UserRestMapper;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response.UserResponse;
import com.fernando.ms.users.app.utils.TestUtilUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.ArgumentMatchers.any;
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

    @Test
    @DisplayName("When_UsersAreExists_Expect_UsersInformationReturnSuccessfully")
    void testCreateUser() {
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
}
