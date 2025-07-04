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
    public Mono<User> findById(String id) {
        return userReactiveRepository.findById(id).map(userPersistenceMapper::toUser);
    }

    @Override
    public Mono<User> save(User user) {
        return userPersistenceMapper.toUser(userReactiveRepository.save(userPersistenceMapper.toUserEntity(user)));
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return userReactiveRepository.existsByEmailIgnoreCase(email);
    }


    @Override
    public Mono<Void> delete(String id) {
        return userReactiveRepository.deleteById(id);
    }

    @Override
    public Mono<Boolean> verifyUser(String id) {
        return userReactiveRepository.existsById(id);
    }

    @Override
    public Flux<User> findByIds(Iterable<String> ids) {
        return userPersistenceMapper.toUsers(userReactiveRepository.findAllById(ids));
    }

    @Override
    public Mono<Boolean> verifyUserByUserId(String id) {
        return userReactiveRepository.existsByUserIdIgnoreCase(id);
    }

    @Override
    public Mono<User> findByUserId(String userId) {
        return userReactiveRepository.findByUserId(userId).map(userPersistenceMapper::toUser);
    }

    @Override
    public Flux<User> findUserByFullName(String fullName, int page, int size) {
        return userPersistenceMapper.toUsers(userReactiveRepository.findAllByFullNamePagination(fullName,page,size));
    }


}
