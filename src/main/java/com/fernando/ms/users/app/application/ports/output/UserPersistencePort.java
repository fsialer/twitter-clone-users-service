package com.fernando.ms.users.app.application.ports.output;

import com.fernando.ms.users.app.domain.models.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserPersistencePort {
    Flux<User> findAll();
    Mono<User> finById(Long id);
}
