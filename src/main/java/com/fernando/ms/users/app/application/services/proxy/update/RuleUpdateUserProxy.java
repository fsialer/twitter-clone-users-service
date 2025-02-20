package com.fernando.ms.users.app.application.services.proxy.update;

import com.fernando.ms.users.app.application.ports.output.UserPersistencePort;
import com.fernando.ms.users.app.application.services.proxy.interfaces.IProcessUser;
import com.fernando.ms.users.app.domain.exceptions.UserNotFoundException;
import com.fernando.ms.users.app.domain.models.User;
import reactor.core.publisher.Mono;

public class RuleUpdateUserProxy implements IProcessUser {
    private final UserPersistencePort userPersistencePort;
    private final Long id;
    private final IProcessUser iProcessUser;

    public RuleUpdateUserProxy(UserPersistencePort userPersistencePort,Long id){
            this.userPersistencePort=userPersistencePort;
            this.id=id;
            this.iProcessUser=new ValidEmailUser(userPersistencePort);
    }
    @Override
    public Mono<User> doProcess(User user) {
        return userPersistencePort.finById(id)
                .switchIfEmpty(Mono.error(UserNotFoundException::new))
                .flatMap(userInfo->{
                    userInfo.setNames(user.getNames());
                    if(!userInfo.getEmail().equals(user.getEmail())) {
                        userInfo.setEmail(user.getEmail());
                        return iProcessUser.doProcess(userInfo);
                    }
                    return Mono.just(user);
                });
    }
}
