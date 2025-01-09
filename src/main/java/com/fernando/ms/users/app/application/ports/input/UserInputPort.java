package com.fernando.ms.users.app.application.ports.input;

import com.fernando.ms.users.app.domain.models.User;
import reactor.core.publisher.Flux;

public interface UserInputPort {
    Flux<User> findAll();
}
