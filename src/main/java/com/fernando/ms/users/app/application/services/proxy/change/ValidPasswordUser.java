package com.fernando.ms.users.app.application.services.proxy.change;

import com.fernando.ms.users.app.application.services.proxy.interfaces.IProcessUser;
import com.fernando.ms.users.app.domain.exceptions.CredentialFailedException;
import com.fernando.ms.users.app.domain.models.User;
import com.fernando.ms.users.app.infrastructure.utils.PasswordUtils;
import reactor.core.publisher.Mono;

public class ValidPasswordUser implements IProcessUser {
    private final IProcessUser iProcessUser;
    public ValidPasswordUser(){
        this.iProcessUser=new ConfirmPasswordChangeUser();
    }
    @Override
    public Mono<User> doProcess(User user) {
        PasswordUtils passwordUtils=new PasswordUtils();
        if(passwordUtils.validatePassword(user.getPassword(), user.getPasswordSalt(), user.getPasswordHash())){
            return Mono.error(CredentialFailedException::new);
        }
        return iProcessUser.doProcess(user);
    }
}
