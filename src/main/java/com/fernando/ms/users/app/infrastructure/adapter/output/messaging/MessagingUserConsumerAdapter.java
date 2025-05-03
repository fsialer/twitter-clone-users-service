package com.fernando.ms.users.app.infrastructure.adapter.output.messaging;

import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.messaging.servicebus.ServiceBusReceiverAsyncClient;

import com.fasterxml.jackson.databind.ObjectMapper;
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
                .map(this::convertMessageToUser)
                .doOnNext(userSink::tryEmitNext)
                .doOnComplete(this::close)
                .subscribe();
    }

    private void close() {
        receiverClient.close();
    }

    private User convertMessageToUser(ServiceBusReceivedMessage message) {
        try{
            String userJson = message.getBody().toString();
            return new ObjectMapper().readValue(userJson, User.class);
        }catch(Exception e){
            log.error("An occurred error: {}", e.getMessage());
        }
        return null;
    }

    public Flux<User> receiveUsers() {
        return  userSink.asFlux();
    }
}
