package com.fernando.ms.users.app.application.services.proxy.change;

import com.fernando.ms.users.app.application.ports.output.UserPersistencePort;
import com.fernando.ms.users.app.application.services.proxy.interfaces.IProcessUser;
import com.fernando.ms.users.app.domain.exceptions.UserNotFoundException;
import com.fernando.ms.users.app.domain.models.User;
import reactor.core.publisher.Mono;

public class RuleChangePasswordUserProxy implements IProcessUser {
    private final UserPersistencePort userPersistencePort;
    private final IProcessUser iProcessUser;
    private final Long id;
    public RuleChangePasswordUserProxy(UserPersistencePort userPersistencePort,Long id){
        this.iProcessUser=new ValidPasswordUser();
        this.userPersistencePort= userPersistencePort;
        this.id=id;
    }
    @Override
    public Mono<User> doProcess(User user) {
        return userPersistencePort.finById(id)
                .switchIfEmpty(Mono.error(UserNotFoundException::new))
                .flatMap(iProcessUser::doProcess);
    }
}
