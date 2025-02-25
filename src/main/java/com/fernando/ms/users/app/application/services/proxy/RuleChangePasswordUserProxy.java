package com.fernando.ms.users.app.application.services.proxy;

import com.fernando.ms.users.app.application.ports.output.UserPersistencePort;
import com.fernando.ms.users.app.domain.exceptions.CredentialFailedException;
import com.fernando.ms.users.app.domain.exceptions.PasswordNotConfirmException;
import com.fernando.ms.users.app.domain.exceptions.UserNotFoundException;
import com.fernando.ms.users.app.domain.models.User;
import com.fernando.ms.users.app.infrastructure.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RuleChangePasswordUserProxy implements IProcessUser {
    private final UserPersistencePort userPersistencePort;
    private final Long id;

    @Override
    public Mono<User> doProcess(User user) {
        return userPersistencePort.finById(id)
                .switchIfEmpty(Mono.error(UserNotFoundException::new))
                .flatMap(userInfo->{
                    if(!user.getNewPassword().equals(user.getConfirmPassword())){
                        return Mono.error(PasswordNotConfirmException::new);
                    }
                    PasswordUtils passwordUtils=new PasswordUtils();
                    if(passwordUtils.validatePassword(user.getPassword(), user.getPasswordSalt(), user.getPasswordHash())){
                        return Mono.error(CredentialFailedException::new);
                    }
                    return Mono.just(userInfo);
                });
    }
}
