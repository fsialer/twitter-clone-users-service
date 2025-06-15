package com.fernando.ms.users.app.infrastructure.adapter.output.persistence;

import com.fernando.ms.users.app.application.ports.output.FollowPersistencePort;
import com.fernando.ms.users.app.domain.models.Follow;
import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.mapper.FollowPersistenceMapper;
import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FollowPersistenceAdapter implements FollowPersistencePort {
    private final FollowRepository followRepository;
    private final FollowPersistenceMapper followPersistenceMapper;
    @Override
    public Mono<Void> saveUserFollowed(Follow follow) {
        return followRepository.save(followPersistenceMapper.toFollowDocument(follow)).then();
    }

    @Override
    public Mono<Boolean> verifyFollow(String followerId, String followedId) {
        return followRepository.existsByFollowerIdAndFollowedIdIgnoreCase(followerId,followedId);
    }

    @Override
    public Mono<Follow> findByIdAndFollowerId(String id,String followerId) {
        return followRepository.findByIdAndFollowerId(id,followerId).map(followPersistenceMapper::toFollow);
    }

    @Override
    public Mono<Void> delete(String id) {
        return followRepository.deleteById(id);
    }

    @Override
    public Flux<Follow> findFollowedByFollowerId(String followerId) {
        return followPersistenceMapper.toFluxFollow(followRepository.findAllByFollowerId(followerId));
    }
}
