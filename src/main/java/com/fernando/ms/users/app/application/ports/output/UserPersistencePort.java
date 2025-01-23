package com.fernando.ms.users.app.application.ports.output;

import com.fernando.ms.users.app.domain.models.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserPersistencePort {
    Flux<User> findAll();
    Mono<User> finById(Long id);
    Mono<User> save(User user);
    Mono<Boolean> existsByEmail(String email);
    Mono<Boolean> existsByUsername(String username);
    Mono<Void> delete(Long id);
    Mono<User> findByUsername(String username);
    Mono<Boolean> verifyUser(Long id);
    Flux<User> findByIds(Iterable<Long> id);
}
