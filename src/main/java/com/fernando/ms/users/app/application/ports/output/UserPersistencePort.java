package com.fernando.ms.users.app.application.ports.output;

import com.fernando.ms.users.app.domain.models.User;
import reactor.core.publisher.Flux;

public interface UserPersistencePort {
    Flux<User> findAll();
}
