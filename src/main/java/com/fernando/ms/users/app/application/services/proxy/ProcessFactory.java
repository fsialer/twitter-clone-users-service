package com.fernando.ms.users.app.application.services.proxy;

import com.fernando.ms.users.app.application.ports.output.UserPersistencePort;

public class ProcessFactory {
    private ProcessFactory() {
    }
    public static IProcessUser validateSave(UserPersistencePort userPersistencePort) {
        return new RuleSaveUserProxy(userPersistencePort);
    }

    public static IProcessUser validateUpdate(UserPersistencePort userPersistencePort,Long id){
        return new RuleUpdateUserProxy(userPersistencePort,id);
    }

    public static IProcessUser validateChangePassword(UserPersistencePort userPersistencePort,Long id){
        return new RuleChangePasswordUserProxy(userPersistencePort,id);
    }

    public static IProcessUser validateAuthentication(UserPersistencePort userPersistencePort){
        return new RuleAuthenticationProxy(userPersistencePort);
    }
}
