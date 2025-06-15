package com.fernando.ms.users.app.infrastructure.adapter.output.persistence.repository;

import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.models.UserEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserReactiveRepository extends ReactiveMongoRepository<UserEntity,String>,UserRepositoryCustom {
    Mono<Boolean> existsByEmailIgnoreCase(String email);
    Mono<Boolean> existsByUserIdIgnoreCase(String id);
    Mono<UserEntity> findByUserId(String email);
}
