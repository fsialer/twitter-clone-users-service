package com.fernando.ms.users.app.application.services.proxy;

import com.fernando.ms.users.app.application.services.proxy.interfaces.IProcessUser;
import com.fernando.ms.users.app.domain.models.User;
import com.fernando.ms.users.app.infrastructure.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class EncryptPasswordProcessUser implements IProcessUser {

    @Override
    public Mono<User> doProcess(User user) {
        PasswordUtils passwordUtils =new PasswordUtils();
        String salt= passwordUtils.generateSalt();
        user.setPasswordHash(passwordUtils.hashPassword(user.getPassword(),salt));
        user.setPasswordSalt(salt);
        return Mono.just(user);
    }
}
