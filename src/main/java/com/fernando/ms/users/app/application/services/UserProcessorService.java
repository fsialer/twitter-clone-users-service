package com.fernando.ms.users.app.application.services;

import com.fernando.ms.users.app.application.ports.input.UserProcessorInputPort;
import com.fernando.ms.users.app.application.ports.output.MessagingUserConsumerPort;
import com.fernando.ms.users.app.application.ports.output.UserPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProcessorService implements UserProcessorInputPort {
    private final UserPersistencePort userPersistencePort;
    private final MessagingUserConsumerPort messagingUserConsumerPort;

    @EventListener(ApplicationReadyEvent.class)
    public void processUser() {
        messagingUserConsumerPort.receiveUsers()
                .flatMap(user -> userPersistencePort.save(user)
                        .doOnSuccess(savedUser -> log.info("User save: {}",savedUser.getEmail()))
                        .doOnError(error -> log.error("An occurred error: {}",error.getMessage()))
                )
                .subscribe();
    }

}
