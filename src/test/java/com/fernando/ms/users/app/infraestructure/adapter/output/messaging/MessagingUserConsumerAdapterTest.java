package com.fernando.ms.users.app.infraestructure.adapter.output.messaging;

import com.azure.core.util.BinaryData;
import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.messaging.servicebus.ServiceBusReceiverAsyncClient;

import com.fernando.ms.users.app.domain.models.User;
import com.fernando.ms.users.app.infrastructure.adapter.output.messaging.MessagingUserConsumerAdapter;
import com.fernando.ms.users.app.utils.TestUtilUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessagingUserConsumerAdapterTest {
    @InjectMocks
    private MessagingUserConsumerAdapter adapter;

    @Mock
    private Sinks.Many<User> userSink;

    @Mock
    private ServiceBusReceivedMessage serviceBusReceivedMessage;

    @Mock
    private ServiceBusReceiverAsyncClient serviceBusReceiverAsyncClient;


    @Test
    @DisplayName("When Message User Exists Expect ReceiveMessage")
    void When_MessageUserExists_Expect_ReceiveMessage() {
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

    @Test
    @DisplayName("When Message Exists Expect ReceiveMessage")
    void When_MessageExists_Expect_ReceiveMessage() {
        User expectedUser = TestUtilUser.buildUserMock();
        when(serviceBusReceivedMessage.getBody()).thenReturn(BinaryData.fromObject(expectedUser));
        when(serviceBusReceiverAsyncClient.receiveMessages()).thenReturn(Flux.just(serviceBusReceivedMessage));
        when(userSink.asFlux()).thenReturn(Flux.just(expectedUser));
        adapter.startListening();
        userSink.tryEmitComplete();
        StepVerifier.create(adapter.receiveUsers())
                .expectNextMatches(user -> user.getNames().equals("Fernando"))
                .thenCancel()
                .verify();
        verify(serviceBusReceiverAsyncClient, times(1)).receiveMessages();
        verify(serviceBusReceivedMessage, times(2)).getBody();
        verify(userSink, times(1)).asFlux();
    }

    @Test
    @DisplayName("Expect Exception When Message Receive Is Different User")
    void Expect_Exception_When_MessageReceiveIsDifferentUser() {
        userSink = Sinks.many().unicast().onBackpressureBuffer();
        adapter = new MessagingUserConsumerAdapter(serviceBusReceiverAsyncClient, userSink);
        String invalidJson = "\"invalid\"";
        when(serviceBusReceivedMessage.getBody()).thenReturn(BinaryData.fromString(invalidJson));
        when(serviceBusReceiverAsyncClient.receiveMessages()).thenReturn(Flux.just(serviceBusReceivedMessage));
        adapter.startListening();
        StepVerifier.create(adapter.receiveUsers())
                .expectSubscription()
                .expectTimeout(Duration.ofMillis(200))
                .verify();
        verify(serviceBusReceiverAsyncClient, times(1)).receiveMessages();
        verify(serviceBusReceivedMessage, times(2)).getBody();
    }
}
