package com.fernando.ms.users.app.application.services;

import com.fernando.ms.users.app.application.ports.input.UserInputPort;
import com.fernando.ms.users.app.application.ports.output.UserPersistencePort;
import com.fernando.ms.users.app.application.services.proxy.authentication.RuleAuthenticationProxy;
import com.fernando.ms.users.app.application.services.proxy.change.RuleChangePasswordUserProxy;
import com.fernando.ms.users.app.application.services.proxy.save.RuleSaveUserProxy;
import com.fernando.ms.users.app.application.services.proxy.update.RuleUpdateUserProxy;
import com.fernando.ms.users.app.domain.exceptions.*;
import com.fernando.ms.users.app.domain.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService implements UserInputPort {
    private final UserPersistencePort userPersistencePort;

    @Override
    public Flux<User> findAll() {
        return userPersistencePort.findAll();
    }

    @Override
    public Mono<User> finById(Long id) {
        return userPersistencePort.finById(id).switchIfEmpty(Mono.error(UserNotFoundException::new));
    }

    @Override
    public Mono<User> save(User user) {
        RuleSaveUserProxy ruleSaveUserProxy =new RuleSaveUserProxy(userPersistencePort);
        return ruleSaveUserProxy.doProcess(user).flatMap(userPersistencePort::save);
    }

    @Override
    public Mono<User> update(Long id, User user) {
        RuleUpdateUserProxy ruleUpdateUserProxy=new RuleUpdateUserProxy(userPersistencePort,id);
        return ruleUpdateUserProxy.doProcess(user).flatMap(userPersistencePort::save);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return userPersistencePort.finById(id)
                .switchIfEmpty(Mono.error(UserNotFoundException::new))
                .flatMap(user->userPersistencePort.delete(id));
    }

    @Override
    public Mono<User> changePassword(Long id, User user) {
        RuleChangePasswordUserProxy ruleChangePasswordUserProxy =new RuleChangePasswordUserProxy(userPersistencePort,id);
        return ruleChangePasswordUserProxy.doProcess(user).flatMap(userPersistencePort::save);
    }

    @Override
    public Mono<User> authentication(User user) {
        RuleAuthenticationProxy ruleAuthenticationProxy=new RuleAuthenticationProxy(userPersistencePort);
        return ruleAuthenticationProxy.doProcess(user);
    }

    @Override
    public Mono<Boolean> verifyUser(Long id) {
        return userPersistencePort.verifyUser(id);
    }

    @Override
    public Flux<User> findByIds(Iterable<Long> ids) {
        return userPersistencePort.findByIds(ids);
    }

    @Override
    public Mono<User> findByUsername(String username) {
        return userPersistencePort.findByUsername(username)
                .switchIfEmpty(Mono.error(UserNotFoundException::new));
    }
}
