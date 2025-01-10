package com.fernando.ms.users.app.application.ports.input;

import com.fernando.ms.users.app.domain.models.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserInputPort {
    Flux<User> findAll();
    Mono<User> finById(Long id);
    Mono<User> save(User user);
}
   