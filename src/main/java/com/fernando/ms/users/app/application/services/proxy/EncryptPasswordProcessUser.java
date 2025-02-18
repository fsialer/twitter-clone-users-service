package com.fernando.ms.users.app.application.services.proxy;

import com.fernando.ms.users.app.domain.models.User;
import com.fernando.ms.users.app.infrastructure.utils.PasswordUtilsImpl;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class EncryptPasswordProcessUser implements IProcessUser {

    @Override
    public Mono<User> doProcess(User user) {
        PasswordUtilsImpl passwordUtilsImpl =new PasswordUtilsImpl();
        String salt= passwordUtilsImpl.generateSalt();
        user.setPasswordHash(passwordUtilsImpl.hashPassword(user.getPassword(),salt));
        user.setPasswordSalt(salt);
        return Mono.just(user);
    }
}
