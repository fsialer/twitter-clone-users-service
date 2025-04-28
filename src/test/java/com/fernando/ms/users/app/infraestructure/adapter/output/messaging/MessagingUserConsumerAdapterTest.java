package com.fernando.ms.users.app.infraestructure.adapter.output.messaging;

import com.fernando.ms.users.app.domain.models.User;
import com.fernando.ms.users.app.infrastructure.adapter.output.messaging.MessagingUserConsumerAdapter;
import com.fernando.ms.users.app.utils.TestUtilUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessagingUserConsumerAdapterTest {
    @InjectMocks
    private MessagingUserConsumerAdapter adapter;
    @Mock
    private Sinks.Many<User> userSink;

    @Test
    void receiveUsers_ShouldEmitUsers() {
        User user = TestUtilUser.buildUserMock();
        when(userSink.asFlux()).thenReturn(Flux.just(user));
        Flux<User> userFlux = adapter.receiveUsers();
        StepVerifier.create(userFlux)
                .consumeNextWith(emitedUser->
                        {
                            assertEquals(emitedUser.getEmail(), user.getEmail());
                            assertEquals(emitedUser.getNames(), user.getNames());
                            assertEquals(emitedUser.getUserId(), user.getUserId());
                        }
                )
                .verifyComplete();
        verify(userSink,times(1)).asFlux();

    }
}
