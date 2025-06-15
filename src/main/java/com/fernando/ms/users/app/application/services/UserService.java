package com.fernando.ms.users.app.application.services;

import com.fernando.ms.users.app.application.ports.input.UserInputPort;
import com.fernando.ms.users.app.application.ports.output.FollowPersistencePort;
import com.fernando.ms.users.app.application.ports.output.UserPersistencePort;
import com.fernando.ms.users.app.domain.exceptions.*;
import com.fernando.ms.users.app.domain.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService implements UserInputPort {
    private final UserPersistencePort userPersistencePort;
    private final FollowPersistencePort followPersistencePort;

    @Override
    public Flux<User> findAll() {
        return userPersistencePort.findAll();
    }

    @Override
    public Mono<User> findById(String id) {
        return userPersistencePort.findById(id).switchIfEmpty(Mono.error(UserNotFoundException::new));
    }

    @Override
    public Mono<User> save(User user) {
        return userPersistencePort.existsByEmail(user.getEmail())
                            .flatMap(existByEmail-> {
                                        if (Boolean.TRUE.equals(existByEmail)) {
                                            return Mono.error(new UserEmailAlreadyExistsException(user.getEmail()));
                                        }
                                        user.setFullName(user.getNames().concat(" ").concat(user.getLastNames()));
                                        return userPersistencePort.save(user);
                                    }
                            );
    }

    @Override
    public Mono<User> update(String id, User user) {
       return userPersistencePort.findById(id)
                .switchIfEmpty(Mono.error(UserNotFoundException::new))
                .flatMap(userInfo->{
                    userInfo.setNames(user.getNames());
                    userInfo.setLastNames(user.getLastNames());
                    return Mono.just(userInfo)
                            .filter(user1 -> !user1.getEmail().equals(user.getEmail()))
                            .flatMap(user1-> userPersistencePort.existsByEmail(user.getEmail())
                                    .flatMap(existByEmail-> {
                                        if (Boolean.TRUE.equals(existByEmail)) {
                                            return Mono.error(new UserEmailAlreadyExistsException(user.getEmail()));
                                        }
                                        user1.setEmail(user.getEmail());
                                        return userPersistencePort.save(user1);
                                    })
                            )
                            .switchIfEmpty(Mono.defer(()->userPersistencePort.save(userInfo)));
                });
    }

    @Override
    public Mono<Void> delete(String id) {
        return userPersistencePort.findById(id)
                .switchIfEmpty(Mono.error(UserNotFoundException::new))
                .flatMap(user->userPersistencePort.delete(id));
    }

    @Override
    public Mono<Boolean> verifyUser(String id) {
        return userPersistencePort.verifyUser(id);
    }

    @Override
    public Flux<User> findByIds(Iterable<String> ids) {
        return userPersistencePort.findByIds(ids);
    }

    @Override
    public Mono<User> findByUserId(String userId) {
        return userPersistencePort.findByUserId(userId).switchIfEmpty(Mono.error(UserNotFoundException::new));
    }

    @Override
    public Mono<User> updateByUserId(String userId, User user) {
        return userPersistencePort.findByUserId(userId)
                .switchIfEmpty(Mono.error(UserNotFoundException::new))
                .flatMap(userInfo->{
                    userInfo.setNames(user.getNames());
                    userInfo.setLastNames(user.getLastNames());
                    return Mono.just(userInfo)
                            .filter(user1 -> !user1.getEmail().equals(user.getEmail()))
                            .flatMap(user1-> userPersistencePort.existsByEmail(user.getEmail())
                                    .flatMap(existByEmail-> {
                                        if (Boolean.TRUE.equals(existByEmail)) {
                                            return Mono.error(new UserEmailAlreadyExistsException(user.getEmail()));
                                        }
                                        user1.setEmail(user.getEmail());
                                        return userPersistencePort.save(user1);
                                    })
                            )
                            .switchIfEmpty(Mono.defer(()->userPersistencePort.save(userInfo)));
                });
    }

    @Override
    public Flux<User> findUserFollowed(String userId) {
        return followPersistencePort.findFollowedByFollowerId(userId)
                .flatMap(follow -> userPersistencePort.findByUserId(follow.getFollowedId()));
    }

    @Override
    public Flux<User> findUserByFullName(String fullName, int page, int size) {
        return userPersistencePort.findUserByFullName(fullName,page,size);
    }
}
