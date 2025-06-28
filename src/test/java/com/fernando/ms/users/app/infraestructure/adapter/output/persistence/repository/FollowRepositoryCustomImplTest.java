package com.fernando.ms.users.app.infraestructure.adapter.output.persistence.repository;

import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.models.UserEntity;
import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.repository.FollowRepositoryCustomImpl;
import com.fernando.ms.users.app.utils.TestUtilUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowRepositoryCustomImplTest {
    @Mock
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @InjectMocks
    private FollowRepositoryCustomImpl followRepositoryCustom;

    @Test
    @DisplayName("When Find Followers By Followed Expect Quantity Followers")
    void When_FindFollowersByFollowed_Expect_QuantityFollowers() {
        when(reactiveMongoTemplate.count(any(Query.class), any(Class.class)))
                .thenReturn(Mono.just(1L));

        Mono<Long> result = followRepositoryCustom.countFollowersByFollowedId("dsd47845s4d");

        StepVerifier.create(result)
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    @DisplayName("When Find Followed By Follower Expect Quantity Followed")
    void When_FindFollowedByFollower_Expect_QuantityFollowed() {
        when(reactiveMongoTemplate.count(any(Query.class), any(Class.class)))
                .thenReturn(Mono.just(1L));

        Mono<Long> result = followRepositoryCustom.countFollowedByFollowerId("dsd47845s4d");

        StepVerifier.create(result)
                .expectNext(1L)
                .verifyComplete();
    }
}
