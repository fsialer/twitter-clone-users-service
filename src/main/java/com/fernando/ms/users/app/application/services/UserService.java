package com.fernando.ms.users.app.application.services;

import com.fernando.ms.users.app.application.ports.input.UserInputPort;
import com.fernando.ms.users.app.application.ports.output.UserPersistencePort;
import com.fernando.ms.users.app.domain.exceptions.*;
import com.fernando.ms.users.app.domain.models.User;
import com.fernando.ms.users.app.infrastructure.adapter.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.SQLOutput;

@Service
@RequiredArgsConstructor
@Slf4j
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

    @Override
    public Mono<User> update(Long id, User user) {
        return userPersistencePort.finById(id)
                .switchIfEmpty(Mono.error(UserNotFoundException::new))
                .flatMap(userInfo->{
                    userInfo.setNames(user.getNames());
                    if(!userInfo.getEmail().equals(user.getEmail())){
                        return userPersistencePort.existsByEmail(user.getEmail())
                                .flatMap(existsEmail->{
                                    if(Boolean.TRUE.equals(existsEmail)){
                                        return Mono.error(new UserEmailAlreadyExistsException(user.getEmail()));
                                    }
                                    userInfo.setEmail(user.getEmail());
                                   return userPersistencePort.save(userInfo);
                                });
                    }
                    return userPersistencePort.save(userInfo);
                });
    }

    @Override
    public Mono<Void> delete(Long id) {
        return userPersistencePort.finById(id)
                .switchIfEmpty(Mono.error(UserNotFoundException::new))
                .flatMap(user->userPersistencePort.delete(id));
    }

    @Override
    public Mono<User> changePassword(Long id, User user) {
        return userPersistencePort.finById(id)
                .switchIfEmpty(Mono.error(UserNotFoundException::new))
                .flatMap(userInfo->{

                    if(!passwordUtils.validatePassword(user.getPassword(),userInfo.getPasswordSalt(),userInfo.getPasswordHash())){
                        return Mono.error(CredentialFailedException::new);
                    }
                    if(!user.getNewPassword().equals(user.getConfirmPassword())){
                        return Mono.error(PasswordNotConfirmException::new);
                    }
                    String salt= passwordUtils.generateSalt();
                    userInfo.setPasswordHash(passwordUtils.hashPassword(user.getNewPassword(),salt));
                    userInfo.setPasswordSalt(salt);
                    return userPersistencePort.save(userInfo);
                });
    }
}
