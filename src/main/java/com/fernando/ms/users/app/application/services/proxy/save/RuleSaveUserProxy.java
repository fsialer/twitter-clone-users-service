package com.fernando.ms.users.app.application.services.proxy.save;

import com.fernando.ms.users.app.application.ports.output.UserPersistencePort;
import com.fernando.ms.users.app.application.services.proxy.interfaces.IProcessUser;
import com.fernando.ms.users.app.domain.exceptions.UserUsernameAlreadyExistsException;
import com.fernando.ms.users.app.domain.models.User;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RuleSaveUserProxy implements IProcessUser {
    private final UserPersistencePort userPersistencePort;
    private final IProcessUser iProcessUser;

    public RuleSaveUserProxy(UserPersistencePort userPersistencePort){
        this.userPersistencePort= userPersistencePort;
        this.iProcessUser=new EmailProcessUser(userPersistencePort);
    }

    @Override
    public Mono<User> doProcess(User user) {
        return userPersistencePort.existsByUsername(user.getUsername())
                .flatMap(existByUsername->{
                    if(Boolean.TRUE.equals(existByUsername)){
                        return Mono.error(new UserUsernameAlreadyExistsException(user.getUsername()));
                    }
                    return iProcessUser.doProcess(user);
                });
    }
}
