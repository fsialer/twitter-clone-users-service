package com.fernando.ms.users.app.application.services;

import com.fernando.ms.users.app.application.ports.input.UserInputPort;
import com.fernando.ms.users.app.application.ports.output.UserPersistencePort;
import com.fernando.ms.users.app.domain.exceptions.UserNotFoundException;
import com.fernando.ms.users.app.domain.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService implements UserInputPort {
    private final UserPersistencePort userPersistencePort;
    @Override
    public Flux<User> findAll() {
        return userPersistencePort.findAll();
    }

    @Override
    public Mono<User> finById(Long id) {
        return userPersistencePort.finById(id).switchIfEmpty(Mono.error(UserNotFoundException::new));
    }
}
