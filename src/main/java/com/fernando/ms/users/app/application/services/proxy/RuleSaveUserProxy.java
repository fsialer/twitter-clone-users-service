package com.fernando.ms.users.app.application.services.proxy;

import com.fernando.ms.users.app.application.ports.output.UserPersistencePort;
import com.fernando.ms.users.app.domain.exceptions.UserEmailAlreadyExistsException;
import com.fernando.ms.users.app.domain.exceptions.UserUsernameAlreadyExistsException;
import com.fernando.ms.users.app.domain.models.User;
import com.fernando.ms.users.app.infrastructure.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RuleSaveUserProxy implements IProcessUser {
    private final UserPersistencePort userPersistencePort;

    @Override
    public Mono<User> doProcess(User user) {
        return userPersistencePort.existsByUsername(user.getUsername())
                .flatMap(existByUsername->{
                    if(Boolean.TRUE.equals(existByUsername)){
                        return Mono.error(new UserUsernameAlreadyExistsException(user.getUsername()));
                    }
                    return userPersistencePort.existsByEmail(user.getEmail())
                            .flatMap(existByEmail-> {
                                        if (Boolean.TRUE.equals(existByEmail)) {
                                            return Mono.error(new UserEmailAlreadyExistsException(user.getEmail()));
                                        }
                                        PasswordUtils passwordUtils =new PasswordUtils();
                                        String salt= passwordUtils.generateSalt();
                                        user.setPasswordHash(passwordUtils.hashPassword(user.getPassword(),salt));
                                        user.setPasswordSalt(salt);
                                        return Mono.just(user);
                                    }
                            );
                });
    }
}
