package com.fernando.ms.users.app.application.services.proxy.authentication;

import com.fernando.ms.users.app.application.ports.output.UserPersistencePort;
import com.fernando.ms.users.app.application.services.proxy.interfaces.IProcessUser;
import com.fernando.ms.users.app.domain.exceptions.UserNotFoundException;
import com.fernando.ms.users.app.domain.models.User;
import reactor.core.publisher.Mono;

public class RuleAuthenticationProxy implements IProcessUser {
    private final UserPersistencePort userPersistencePort;
    private final IProcessUser iProcessUser;

    public RuleAuthenticationProxy(UserPersistencePort userPersistencePort){
        this.userPersistencePort=userPersistencePort;
        this.iProcessUser=new ValidPasswordAuthentication();
    }
    @Override
    public Mono<User> doProcess(User user) {
        return userPersistencePort.findByUsername(user.getUsername())
                .switchIfEmpty(Mono.error(UserNotFoundException::new))
                .flatMap(iProcessUser::doProcess);
    }
}
