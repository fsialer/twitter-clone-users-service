package com.fernando.ms.users.app.application.ports.output;

import com.fernando.ms.users.app.domain.models.Follow;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FollowPersistencePort {
    Mono<Void> saveUserFollowed(Follow follow);
    Mono<Boolean> verifyFollow(String followerId,String followedId);
    Mono<Follow> findByIdAndFollowerId(String id,String followerId);
    Mono<Void> delete(String id);
    Flux<Follow> findFollowedByFollowerId(String followerId);
    Mono<Long> countFollowers(String userId);
    Mono<Long> countFollowed(String userId);
}
