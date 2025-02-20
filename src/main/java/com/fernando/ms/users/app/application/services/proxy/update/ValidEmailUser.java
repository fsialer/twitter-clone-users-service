package com.fernando.ms.users.app.application.services.proxy.update;

import com.fernando.ms.users.app.application.ports.output.UserPersistencePort;
import com.fernando.ms.users.app.application.services.proxy.interfaces.IProcessUser;
import com.fernando.ms.users.app.domain.exceptions.UserEmailAlreadyExistsException;
import com.fernando.ms.users.app.domain.models.User;
import reactor.core.publisher.Mono;

public class ValidEmailUser implements IProcessUser {
    private final UserPersistencePort userPersistencePort;

    public ValidEmailUser(UserPersistencePort userPersistencePort){
        this.userPersistencePort=userPersistencePort;
    }

    @Override
    public Mono<User> doProcess(User user) {
        return userPersistencePort.existsByEmail(user.getEmail())
                .flatMap(existByEmail-> {
                            if (Boolean.TRUE.equals(existByEmail)) {
                                return Mono.error(new UserEmailAlreadyExistsException(user.getEmail()));
                            }
                           return Mono.just(user);
                        }
                );
    }
}
