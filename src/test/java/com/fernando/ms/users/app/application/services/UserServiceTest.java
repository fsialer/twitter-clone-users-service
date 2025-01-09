package com.fernando.ms.users.app.application.services;

import com.fernando.ms.users.app.application.ports.output.UserPersistencePort;
import com.fernando.ms.users.app.domain.models.User;
import com.fernando.ms.users.app.utils.TestUtilUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserPersistencePort userPersistencePort;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("When Users Information Is Correct Expect A List Users")
    void When_UsersInformationIsCorrect_Expect_AListUsers(){
        User user= TestUtilUser.buildUserMock();
        when(userPersistencePort.findAll()).thenReturn(Flux.just(user));

        Flux<User> users=userService.findAll();
        StepVerifier.create(users)
                .expectNext(user)
                .verifyComplete();
        Mockito.verify(userPersistencePort,times(1)).findAll();
    }

}
