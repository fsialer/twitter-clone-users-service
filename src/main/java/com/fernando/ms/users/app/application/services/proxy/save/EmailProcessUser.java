package com.fernando.ms.users.app.application.services.proxy.save;

import com.fernando.ms.users.app.application.ports.output.UserPersistencePort;
import com.fernando.ms.users.app.application.services.proxy.interfaces.IProcessUser;
import com.fernando.ms.users.app.application.services.proxy.EncryptPasswordProcessUser;
import com.fernando.ms.users.app.domain.exceptions.UserEmailAlreadyExistsException;
import com.fernando.ms.users.app.domain.models.User;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class EmailProcessUser implements IProcessUser {
    private final UserPersistencePort userPersistencePort;
    private final IProcessUser iProcessUser=new EncryptPasswordProcessUser();

    @Override
    public Mono<User> doProcess(User user) {
        return userPersistencePort.existsByEmail(user.getEmail())
                .flatMap(existByEmail-> {
                            if (Boolean.TRUE.equals(existByEmail)) {
                                return Mono.error(new UserEmailAlreadyExistsException(user.getEmail()));
                            }
                            return iProcessUser.doProcess(user);
                        }
                    );
    }
}
