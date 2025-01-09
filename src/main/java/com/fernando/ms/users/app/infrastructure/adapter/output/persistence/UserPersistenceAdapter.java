package com.fernando.ms.users.app.infrastructure.adapter.output.persistence;

import com.fernando.ms.users.app.application.ports.output.UserPersistencePort;
import com.fernando.ms.users.app.domain.models.User;
import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.mapper.UserPersistenceMapper;
import com.fernando.ms.users.app.infrastructure.adapter.output.persistence.repository.UserReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPersistencePort {

    private final UserReactiveRepository userReactiveRepository;
    private final UserPersistenceMapper userPersistenceMapper;


    @Override
    public Flux<User> findAll() {
        return userPersistenceMapper.toUsers(userReactiveRepository.findAll());
    }

    @Override
    public Mono<User> finById(Long id) {
        return userReactiveRepository.findById(id).map(userPersistenceMapper::toUser);
    }


}
