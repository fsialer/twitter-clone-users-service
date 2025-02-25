package com.fernando.ms.users.app.application.services.proxy;

import com.fernando.ms.users.app.domain.models.User;
import reactor.core.publisher.Mono;

public interface IProcessUser {
    Mono<User> doProcess(User user);
}
