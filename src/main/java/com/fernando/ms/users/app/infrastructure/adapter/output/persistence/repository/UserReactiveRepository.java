package com.fernando.ms.users.app.infrastructure.adapter.output.persistence.repository;

import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.models.UserEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserReactiveRepository extends ReactiveMongoRepository<UserEntity,String> {
    Mono<Boolean> existsByEmailIgnoreCase(String email);
}
