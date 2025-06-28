package com.fernando.ms.users.app.infrastructure.adapter.output.persistence.repository;

import reactor.core.publisher.Mono;

public interface FollowRepositoryCustom {
    Mono<Long> countFollowersByFollowedId(String followedId);
}
