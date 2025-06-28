package com.fernando.ms.users.app.application.services;

import com.fernando.ms.users.app.application.ports.input.FollowInputPort;
import com.fernando.ms.users.app.application.ports.output.FollowPersistencePort;
import com.fernando.ms.users.app.application.ports.output.UserPersistencePort;
import com.fernando.ms.users.app.domain.exceptions.FollowedNotFoundException;
import com.fernando.ms.users.app.domain.exceptions.FollowerNotFoundException;
import com.fernando.ms.users.app.domain.exceptions.UserRuleException;
import com.fernando.ms.users.app.domain.models.Follow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FollowService implements FollowInputPort {
    private final FollowPersistencePort followPersistencePort;
    private final UserPersistencePort userPersistencePort;
    @Override
    public Mono<Void> followUser(Follow follow) {
        if (follow.getFollowerId().equals(follow.getFollowedId())) {
            return Mono.error(new UserRuleException("User cannot follow themselves!"));
        }
        return  userPersistencePort.findByUserId(follow.getFollowerId())
                        .switchIfEmpty(Mono.error(FollowerNotFoundException::new))
                        .flatMap(followerUser->
                             userPersistencePort.findByUserId(follow.getFollowedId())
                                     .switchIfEmpty(Mono.error(FollowedNotFoundException::new))
                        )
                        .flatMap(followedUser->
                            followPersistencePort.verifyFollow(follow.getFollowerId(), follow.getFollowedId())
                                .filter(Boolean.FALSE::equals)
                                .switchIfEmpty(Mono.error(new UserRuleException("You follow this user.")))
                                .flatMap(verify -> followPersistencePort.saveUserFollowed(follow))
                );
    }

    @Override
    public Mono<Void> unFollowUser(String id, String followerId) {
        return followPersistencePort.findByIdAndFollowerId(id,followerId)
                .switchIfEmpty(Mono.error(new UserRuleException("You don't follow this user.")))
                .flatMap(follow -> followPersistencePort.delete(follow.getId()));
    }

    @Override
    public Mono<Boolean> verifyFollow(String userId, String followedId) {
        return followPersistencePort.verifyFollow(userId,followedId);
    }

    @Override
    public Mono<Long> countFollowers(String userId) {
        return followPersistencePort.countFollowers(userId);
    }


}
