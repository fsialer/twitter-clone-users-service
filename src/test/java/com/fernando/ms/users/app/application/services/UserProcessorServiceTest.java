package com.fernando.ms.users.app.application.services;

import com.fernando.ms.users.app.application.ports.output.MessagingUserConsumerPort;
import com.fernando.ms.users.app.application.ports.output.UserPersistencePort;
import com.fernando.ms.users.app.domain.models.User;
import com.fernando.ms.users.app.utils.TestUtilUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProcessorServiceTest {
    @Mock
    private UserPersistencePort userPersistencePort;

    @Mock
    private MessagingUserConsumerPort messagingUserConsumerPort;

    @InjectMocks
    private UserProcessorService userProcessorService;

    @Test
    @DisplayName("When_messageReceiveCorrectly_Expect_SaveUser")
    void When_messageReceiveCorrectly_Expect_SaveUser() {
        User user = TestUtilUser.buildUserMock();

        when(messagingUserConsumerPort.receiveUsers()).thenReturn(Flux.just(user));
        when(userPersistencePort.save(user)).thenReturn(Mono.just(user));

        userProcessorService.processUser();

        verify(messagingUserConsumerPort, times(1)).receiveUsers();
        verify(userPersistencePort, times(1)).save(user);
    }


    @Test
    @DisplayName("Expect_RuntimeException_When_ErrorDuringSave")
    void processUser_ShouldHandleErrorDuringSave() {
        User user = TestUtilUser.buildUserMock();

        when(messagingUserConsumerPort.receiveUsers()).thenReturn(Flux.just(user));
        when(userPersistencePort.save(user)).thenReturn(Mono.error(new RuntimeException("Error al guardar usuario")));

        userProcessorService.processUser();

        verify(messagingUserConsumerPort, times(1)).receiveUsers();
        verify(userPersistencePort, times(1)).save(user);
    }
}
