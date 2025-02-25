package com.fernando.ms.users.app.application.services.proxy;

import com.fernando.ms.users.app.application.ports.output.UserPersistencePort;
import com.fernando.ms.users.app.domain.exceptions.UserEmailAlreadyExistsException;
import com.fernando.ms.users.app.domain.exceptions.UserNotFoundException;
import com.fernando.ms.users.app.domain.models.User;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RuleUpdateUserProxy implements IProcessUser {
    private final UserPersistencePort userPersistencePort;
    private final Long id;

    @Override
    public Mono<User> doProcess(User user) {
        return userPersistencePort.finById(id)
                .switchIfEmpty(Mono.error(UserNotFoundException::new))
                .flatMap(userInfo->{
                    userInfo.setNames(user.getNames());
                    return Mono.just(userInfo)
                            .filter(user1 -> !user1.getEmail().equals(user.getEmail()))
                            .flatMap(user1-> userPersistencePort.existsByEmail(user.getEmail())
                                        .flatMap(existByEmail-> {
                                            if (Boolean.TRUE.equals(existByEmail)) {
                                                return Mono.error(new UserEmailAlreadyExistsException(user.getEmail()));
                                            }
                                            user1.setEmail(user.getEmail());
                                            return Mono.just(user1);
                                        })
                            )
                            .defaultIfEmpty(userInfo);
                });
    }
}
