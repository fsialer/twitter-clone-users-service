package com.fernando.ms.users.app.infrastructure.adapter.output.persistence.repository;

import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.models.FollowDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface FollowRepository extends ReactiveMongoRepository<FollowDocument,String> {
    Mono<Boolean> existsByFollowerIdAndFollowedIdIgnoreCase(String followerId,String followedId);
    Mono<FollowDocument> findByIdAndFollowerId(String id,String followerId);
}
