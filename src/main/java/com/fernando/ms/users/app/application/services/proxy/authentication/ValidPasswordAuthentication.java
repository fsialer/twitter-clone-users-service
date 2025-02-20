package com.fernando.ms.users.app.application.services.proxy.authentication;

import com.fernando.ms.users.app.application.services.proxy.interfaces.IProcessUser;
import com.fernando.ms.users.app.domain.exceptions.CredentialFailedException;
import com.fernando.ms.users.app.domain.models.User;
import com.fernando.ms.users.app.infrastructure.utils.PasswordUtils;
import reactor.core.publisher.Mono;

public class ValidPasswordAuthentication implements IProcessUser {

    @Override
    public Mono<User> doProcess(User user) {
        PasswordUtils passwordUtils=new PasswordUtils();
        if(passwordUtils.validatePassword(user.getPassword(), user.getPasswordSalt(), user.getPasswordHash())){
            return Mono.error(CredentialFailedException::new);
        }
        return Mono.just(user);
    }
}
