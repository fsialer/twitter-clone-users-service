package com.fernando.ms.users.app.application.ports.input;

import com.fernando.ms.users.app.domain.models.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserInputPort {
    Flux<User> findAll();
    Mono<User> finById(Long id);
    Mono<User> save(User user);
    Mono<User> update(Long id,User user);
    Mono<Void> delete(Long id);
    Mono<User> changePassword(Long id, User user);
    Mono<User> authentication(User user);
    Mono<Boolean> verifyUser(Long id);
    Flux<User> findByIds(Iterable<Long> ids);
    Mono<User> findByUsername(String username);
}
   