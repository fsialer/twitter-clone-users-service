package com.fernando.ms.users.app.application.services.proxy.change;

import com.fernando.ms.users.app.application.services.proxy.EncryptPasswordProcessUser;
import com.fernando.ms.users.app.application.services.proxy.interfaces.IProcessUser;
import com.fernando.ms.users.app.domain.exceptions.PasswordNotConfirmException;
import com.fernando.ms.users.app.domain.models.User;
import reactor.core.publisher.Mono;

public class ConfirmPasswordChangeUser implements IProcessUser {
    private final IProcessUser iProcessUser;

    public ConfirmPasswordChangeUser(){
        this.iProcessUser=new EncryptPasswordProcessUser();
    }

    @Override
    public Mono<User> doProcess(User user) {
        if(!user.getNewPassword().equals(user.getConfirmPassword())){
            return Mono.error(PasswordNotConfirmException::new);
        }
        return iProcessUser.doProcess(user);
    }
}
