package com.fernando.ms.users.app.application.services;

import com.fernando.ms.users.app.application.ports.input.UserInputPort;
import com.fernando.ms.users.app.application.ports.output.UserPersistencePort;
import com.fernando.ms.users.app.domain.exceptions.UserEmailAlreadyExistsException;
import com.fernando.ms.users.app.domain.exceptions.UserNotFoundException;
import com.fernando.ms.users.app.domain.exceptions.UserUsernameAlreadyExistsException;
import com.fernando.ms.users.app.domain.models.User;
import com.fernando.ms.users.app.infrastructure.adapter.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService implements UserInputPort {
    private final UserPersistencePort userPersistencePort;
    private final PasswordUtils passwordUtils;
    @Override
    public Flux<User> findAll() {
        return userPersistencePort.findAll();
    }

    @Override
    public Mono<User> finById(Long id) {
        return userPersistencePort.finById(id).switchIfEmpty(Mono.error(UserNotFoundException::new));
    }

    @Override
    public Mono<User> save(User user) {
        return userPersistencePort.existsByUsername(user.getUsername())
                .flatMap(existByUsername->{
                    if(Boolean.TRUE.equals(existByUsername)){
                        return Mono.error(new UserUsernameAlreadyExistsException(user.getUsername()));
                    }
                    return userPersistencePort.existsByEmail(user.getEmail());
                })
                .flatMap(existByEmail->{
                    if(Boolean.TRUE.equals(existByEmail)){
                        return Mono.error(new UserEmailAlreadyExistsException(user.getEmail()));
                    }
                    String salt= passwordUtils.generateSalt();
                    user.setPasswordHash(passwordUtils.hashPassword(user.getPassword(),salt));
                    user.setPasswordSalt(salt);
                    return userPersistencePort.save(user);
                });
    }
}
