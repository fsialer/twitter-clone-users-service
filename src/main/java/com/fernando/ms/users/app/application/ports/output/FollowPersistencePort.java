package com.fernando.ms.users.app.application.ports.output;

import com.fernando.ms.users.app.domain.models.Follow;
import reactor.core.publisher.Mono;

public interface FollowPersistencePort {
    Mono<Void> saveUserFollowed(Follow follow);
    Mono<Boolean> verifyFollow(String followerId,String followedId);
}
