package com.fernando.ms.users.app.infraestructure.adapter.output.persistence.mapper;

import com.fernando.ms.users.app.domain.models.Follow;
import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.mapper.FollowPersistenceMapper;
import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.models.FollowDocument;
import com.fernando.ms.users.app.utils.TestUtilFollow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FollowPersistenceMapperTest {
    private FollowPersistenceMapper followPersistenceMapper;

    @BeforeEach
    void setUp(){
        followPersistenceMapper= Mappers.getMapper(FollowPersistenceMapper.class);
    }

    @Test
    @DisplayName("When Map FollowDocument Expect Follow")
    void When_MapFollowDocument_Expect_Follow(){
        FollowDocument followDocument= TestUtilFollow.buildFollowDocumentMock();
        Follow follow=followPersistenceMapper.toFollow(followDocument);
        assertEquals("68045526dffe6e2de223e55b", follow.getFollowerId());
        assertEquals("fdsfds4544", follow.getFollowedId());
    }

    @Test
    @DisplayName("When Map Follow Expect FollowDocument")
    void When_MapFollow_Expect_FollowDocument(){
        Follow follow= TestUtilFollow.buildFollowMock();
        FollowDocument followDocument=followPersistenceMapper.toFollowDocument(follow);
        assertEquals("68045526dffe6e2de223e55b", followDocument.getFollowerId());
        assertEquals("fdsfds4544", followDocument.getFollowedId());
    }

    @Test
    @DisplayName("When Map FluxFollowDocument Expect FluxFollow")
    void When_MapFluxFollowDocument_Expect_FluxFollow(){
        FollowDocument followDocument= TestUtilFollow.buildFollowDocumentMock();
        Flux<Follow> fluxFollow=followPersistenceMapper.toFluxFollow(Flux.just(followDocument));

        StepVerifier.create(fluxFollow)
                        .consumeNextWith(follow -> {
                            assertEquals("68045526dffe6e2de223e55b", follow.getFollowerId());
                            assertEquals("fdsfds4544", follow.getFollowedId());
                        })
                 .verifyComplete();

    }
}
