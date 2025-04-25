package com.fernando.ms.users.app.application.ports.output;

import com.fernando.ms.users.app.domain.models.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserPersistencePort {
    Flux<User> findAll();
    Mono<User> findById(String id);
    Mono<User> save(User user);
    Mono<Boolean> existsByEmail(String email);
    Mono<Void> delete(String id);
    Mono<Boolean> verifyUser(String id);
    Flux<User> findByIds(Iterable<String> id);
}
