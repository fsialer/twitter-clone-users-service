package com.fernando.ms.users.app.infrastructure.adapter.output.persistence.repository;

import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.models.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserReactiveRepository extends ReactiveCrudRepository<UserEntity,Long> {
    Mono<Boolean> existsByEmailIgnoreCase(String email);
    Mono<Boolean> existsByUsernameIgnoreCase(String username);
    Mono<UserEntity> findByUsername(String username);

}
