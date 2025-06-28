package com.fernando.ms.users.app.application.ports.input;

import com.fernando.ms.users.app.domain.models.Follow;
import reactor.core.publisher.Mono;

public interface FollowInputPort {
    Mono<Void> followUser(Follow follow);
    Mono<Void> unFollowUser(String id,String followerId);
    Mono<Boolean> verifyFollow(String userId, String followedId);
    Mono<Long> countFollowers(String userId);
}
