package com.fernando.ms.users.app.infrastructure.adapter.output.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FollowRepositoryCustomImpl implements FollowRepositoryCustom{
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Mono<Long> countFollowersByFollowedId(String followedId) {
        Query query=new Query(Criteria.where("followedId").is(followedId));
        return reactiveMongoTemplate.count(query, Long.class);
    }

    @Override
    public Mono<Long> countFollowedByFollowerId(String followerId) {
        Query query=new Query(Criteria.where("followerId").is(followerId));
        return reactiveMongoTemplate.count(query, Long.class);
    }
}
