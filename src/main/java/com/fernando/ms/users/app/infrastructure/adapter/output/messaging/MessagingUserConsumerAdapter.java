package com.fernando.ms.users.app.infrastructure.adapter.output.messaging;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.messaging.servicebus.ServiceBusReceiverAsyncClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.ms.users.app.application.ports.output.MessagingUserConsumerPort;
import com.fernando.ms.users.app.domain.models.User;
import com.fernando.ms.users.app.infrastructure.config.ServiceBusProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessagingUserConsumerAdapter implements MessagingUserConsumerPort {
    private ServiceBusReceiverAsyncClient  receiverClient;
    private final Sinks.Many<User> userSink;
    private final ServiceBusProperties serviceBusProperties;

    @PostConstruct
    public void init() {
        receiverClient = new ServiceBusClientBuilder()
                .connectionString(serviceBusProperties.getConnectionString())
                .receiver()
                .queueName(serviceBusProperties.getQueueName())
                .buildAsyncClient();
        startListening();
    }

    private void startListening() {
        receiverClient.receiveMessages()
                .map(this::convertMessageToUser)
                .doOnNext(user->log.info("Received message: {}", user.getEmail()))
                .doOnNext(userSink::tryEmitNext)
                .doOnComplete(this::close)
                .subscribe();
    }

    public void close() {
        if (receiverClient != null) {
            receiverClient.close();
        }
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
