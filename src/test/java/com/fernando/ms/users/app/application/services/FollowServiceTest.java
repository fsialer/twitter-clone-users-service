package com.fernando.ms.users.app.application.services;

import com.fernando.ms.users.app.application.ports.output.FollowPersistencePort;
import com.fernando.ms.users.app.application.ports.output.UserPersistencePort;
import com.fernando.ms.users.app.domain.exceptions.FollowedNotFoundException;
import com.fernando.ms.users.app.domain.exceptions.FollowerNotFoundException;
import com.fernando.ms.users.app.domain.exceptions.UserRuleException;
import com.fernando.ms.users.app.domain.models.Follow;
import com.fernando.ms.users.app.domain.models.User;
import com.fernando.ms.users.app.utils.TestUtilFollow;
import com.fernando.ms.users.app.utils.TestUtilUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FollowServiceTest {
    @Mock
    private FollowPersistencePort followPersistencePort;

    @InjectMocks
    private FollowService followService;

    @Mock
    private UserPersistencePort userPersistencePort;


    @Test
    @DisplayName("Expect UserRuleException When Identifier Of Follower And Followed Are Invalid")
    void Expect_UserRuleException_When_IdentifierOfFollowerAndFollowedAreInvalid() {
        Follow follow = TestUtilFollow.buildFollowMock();
        follow.setFollowedId("68045526dffe6e2de223e55b");

        Mono<Void> result = followService.followUser(follow);

        StepVerifier.create(result)
                .expectError(UserRuleException.class)
                .verify();

        Mockito.verify(followPersistencePort, never()).verifyFollow(anyString(), anyString());
        Mockito.verify(userPersistencePort, never()).findByUserId(anyString());
        Mockito.verify(followPersistencePort, never()).saveUserFollowed(any(Follow.class));
    }

    @Test
    @DisplayName("Expect UserRuleException When FollowIsInvalid")
    void Expect_UserRuleException_When_FollowIsInvalid() {
        Follow follow = TestUtilFollow.buildFollowMock();
        User user= TestUtilUser.buildUserMock();
        User user2= TestUtilUser.buildUserMock();
        user2.setUserId("447dsdsdssssss");


        when(userPersistencePort.findByUserId(follow.getFollowerId()))
                .thenReturn(Mono.just(user));
        when(userPersistencePort.findByUserId(follow.getFollowedId()))
                .thenReturn(Mono.just(user2));
        when(followPersistencePort.verifyFollow(anyString(), anyString()))
                .thenReturn(Mono.just(true));

        Mono<Void> result = followService.followUser(follow);

        StepVerifier.create(result)
                .expectError(UserRuleException.class)
                .verify();

        Mockito.verify(followPersistencePort, times(1)).verifyFollow(anyString(), anyString());
        Mockito.verify(userPersistencePort, times(2)).findByUserId(anyString());
        Mockito.verify(followPersistencePort, never()).saveUserFollowed(any(Follow.class));
    }

    @Test
    @DisplayName("Expect FollowerNotFoundException When Follow Is Invalid")
    void Expect_FollowerNotFoundException_When_FollowIsInvalid() {
        Follow follow = TestUtilFollow.buildFollowMock();

        when(userPersistencePort.findByUserId(follow.getFollowerId()))
                .thenReturn(Mono.empty());

        Mono<Void> result = followService.followUser(follow);

        StepVerifier.create(result)
                .expectError(FollowerNotFoundException.class)
                .verify();

        verify(userPersistencePort, times(1)).findByUserId(follow.getFollowerId());
        verify(userPersistencePort, never()).findByUserId(follow.getFollowedId());
        verify(followPersistencePort, never()).verifyFollow(anyString(), anyString());
    }

    @Test
    @DisplayName("Expect FollowedNotFoundException When FollowIsInvalid")
    void Expect_FollowedNotFoundException_When_FollowIsInvalid() {
        Follow follow = TestUtilFollow.buildFollowMock();
        User user= TestUtilUser.buildUserMock();

        when(userPersistencePort.findByUserId(follow.getFollowerId()))
                .thenReturn(Mono.just(user));

        when(userPersistencePort.findByUserId(follow.getFollowedId()))
                .thenReturn(Mono.empty());

        Mono<Void> result = followService.followUser(follow);

        StepVerifier.create(result)
                .expectError(FollowedNotFoundException.class)
                .verify();

        verify(userPersistencePort, times(1)).findByUserId(follow.getFollowerId());
        verify(userPersistencePort, times(1)).findByUserId(follow.getFollowedId());
        verify(followPersistencePort, never()).verifyFollow(anyString(), anyString());
    }

    @Test
    @DisplayName("When FollowIsValid Expect FollowSavedCorrectly")
    void When_FollowIsValid_Expect_FollowSavedCorrectly() {
        Follow follow = TestUtilFollow.buildFollowMock();
        User user= TestUtilUser.buildUserMock();
        User user2= TestUtilUser.buildUserMock();
        user2.setUserId("447dsdsd");

        when(userPersistencePort.findByUserId(anyString()))
                .thenReturn(Mono.just(user));
        when(userPersistencePort.findByUserId(anyString()))
                .thenReturn(Mono.just(user2));
        when(followPersistencePort.verifyFollow(anyString(), anyString()))
                .thenReturn(Mono.just(false));
        when(followPersistencePort.saveUserFollowed(any(Follow.class)))
                .thenReturn(Mono.empty());

        Mono<Void> result = followService.followUser(follow);

        StepVerifier.create(result)
                .verifyComplete();

        verify(followPersistencePort, times(1)).verifyFollow(anyString(), anyString());
        verify(userPersistencePort, times(2)).findByUserId(anyString());
        verify(followPersistencePort, times(1)).saveUserFollowed(follow);
    }

    @Test
    @DisplayName("When User Follows Another Expect Unfollow Successfully")
    void When_UserFollowsAnother_Expect_UnfollowSuccessfully() {
        Follow follow = TestUtilFollow.buildFollowMock();

        when(followPersistencePort.findByIdAndFollowerId(anyString(), anyString()))
                .thenReturn(Mono.just(follow));
        when(followPersistencePort.delete(anyString()))
                .thenReturn(Mono.empty());

        Mono<Void> result = followService.unFollowUser("followId", "followerId");

        StepVerifier.create(result)
                .verifyComplete();

        verify(followPersistencePort, times(1)).findByIdAndFollowerId(anyString(), anyString());
        verify(followPersistencePort, times(1)).delete(anyString());
    }

    @Test
    @DisplayName("Expect UserRuleException When User Does Not Follow Another")
    void Expect_UserRuleException_When_UserDoesNotFollowAnother_() {
        when(followPersistencePort.findByIdAndFollowerId(anyString(), anyString()))
                .thenReturn(Mono.empty());

        Mono<Void> result = followService.unFollowUser("followId", "followerId");

        StepVerifier.create(result)
                .expectError(UserRuleException.class)
                .verify();

        verify(followPersistencePort, times(1)).findByIdAndFollowerId(anyString(), anyString());
        verify(followPersistencePort, never()).delete(anyString());
    }
}
