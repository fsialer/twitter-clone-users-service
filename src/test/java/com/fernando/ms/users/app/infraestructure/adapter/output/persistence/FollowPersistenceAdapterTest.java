package com.fernando.ms.users.app.infraestructure.adapter.output.persistence;

import com.fernando.ms.users.app.domain.models.Follow;
import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.FollowPersistenceAdapter;
import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.mapper.FollowPersistenceMapper;
import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.models.FollowDocument;
import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.repository.FollowRepository;
import com.fernando.ms.users.app.utils.TestUtilFollow;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FollowPersistenceAdapterTest {

    @Mock
    private FollowRepository followRepository;

    @Mock
    private FollowPersistenceMapper followPersistenceMapper;

    @InjectMocks
    private FollowPersistenceAdapter followPersistenceAdapter;

    @Test
    @DisplayName("When Identifier Of Follower And Followed Existing Expect A Result True")
    void When_IdentifierOfFollowerAndFollowedExisting_Expect_AResultTrue() {
        when(followRepository.existsByFollowerIdAndFollowedIdIgnoreCase(anyString(), anyString()))
                .thenReturn(Mono.just(true));

        Mono<Boolean> result = followPersistenceAdapter.verifyFollow("followerId", "followedId");

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    @DisplayName("When Identifier Of Follower And Followed Not Existing Expect A Result False")
    void When_IdentifierOfFollowerAndFollowedNotExisting_Expect_AResultFalse() {

        when(followRepository.existsByFollowerIdAndFollowedIdIgnoreCase(anyString(), anyString()))
                .thenReturn(Mono.just(false));
        Mono<Boolean> result = followPersistenceAdapter.verifyFollow("followerId", "followedId");
        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    @DisplayName("When Identifier Of Follower And Followed Existing Expect Save Information")
    void When_IdentifierOfFollowerAndFollowedExisting_Expect_SaveInformation() {
        Follow follow = TestUtilFollow.buildFollowMock();
        FollowDocument followDocument = TestUtilFollow.buildFollowDocumentMock();

        when(followPersistenceMapper.toFollowDocument(any(Follow.class))).thenReturn(followDocument); // Simula el documento
        when(followRepository.save(any())).thenReturn(Mono.empty());

        Mono<Void> result = followPersistenceAdapter.saveUserFollowed(follow);

        StepVerifier.create(result)
                .verifyComplete();

        verify(followPersistenceMapper, times(1)).toFollowDocument(any(Follow.class));
        verify(followRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("When Identifier Of Follower And Followed Existing Expect Information Follower")
    void When_IdentifierOfFollowerAndFollowedExisting_Expect_InformationFollower() {
        FollowDocument followDocument = TestUtilFollow.buildFollowDocumentMock();
        Follow follow = TestUtilFollow.buildFollowMock();

        when(followRepository.findByIdAndFollowerId(anyString(), anyString()))
                .thenReturn(Mono.just(followDocument));
        when(followPersistenceMapper.toFollow(followDocument))
                .thenReturn(follow);

        Mono<Follow> result = followPersistenceAdapter.findByIdAndFollowerId("followId", "followerId");

        StepVerifier.create(result)
                .expectNext(follow)
                .verifyComplete();

        verify(followRepository, times(1)).findByIdAndFollowerId(anyString(), anyString());
        verify(followPersistenceMapper, times(1)).toFollow(followDocument);
    }

    @Test
    @DisplayName("When Identifier Of Follower Existing Expect Information Delete Correctly Follower")
    void When_IdentifierOfFollowerExisting_Expect_InformationDeleteCorrectlyFollower() {
        when(followRepository.deleteById(anyString()))
                .thenReturn(Mono.empty());

        Mono<Void> result = followPersistenceAdapter.delete("followId");
        StepVerifier.create(result)
                .verifyComplete();
        verify(followRepository, times(1)).deleteById("followId");
    }

}
