package com.fernando.ms.users.app.application.ports.input;

import com.fernando.ms.users.app.domain.models.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserInputPort {
    Flux<User> findAll();
    Mono<User> findById(String id);
    Mono<User> save(User user);
    Mono<User> update(String id,User user);
    Mono<Void> delete(String id);
    Mono<Boolean> verifyUser(String id);
    Flux<User> findByIds(Iterable<String> ids);
    Mono<User> findByUserId(String userId);
    Mono<User> updateByUserId(String userId,User user);
    Flux<User> findUserFollowed(String userId);
    Flux<User> findUserByFullName(String fullName, int page, int size);
}
   