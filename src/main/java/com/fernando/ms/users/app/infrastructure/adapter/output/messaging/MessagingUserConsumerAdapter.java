package com.fernando.ms.users.app.infrastructure.adapter.output.messaging;

import com.azure.core.util.BinaryData;
import com.azure.messaging.servicebus.ServiceBusReceiverAsyncClient;

import com.fernando.ms.users.app.application.ports.output.MessagingUserConsumerPort;
import com.fernando.ms.users.app.domain.models.User;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessagingUserConsumerAdapter implements MessagingUserConsumerPort {
    private final ServiceBusReceiverAsyncClient  receiverClient;
    private final Sinks.Many<User> userSink;

    @EventListener(ApplicationReadyEvent.class)
    public void startListening() {
        receiverClient.receiveMessages()
                .doOnNext(user->log.info("Received message: {}", user.getBody().toString()))
                .map(message->BinaryData.fromString(message.getBody().toString()).toObject(User.class))
                .doOnNext(userSink::tryEmitNext)
                .doOnComplete(receiverClient::close)
                .subscribe();
    }

    public Flux<User> receiveUsers() {
        return  userSink.asFlux();
    }
}
