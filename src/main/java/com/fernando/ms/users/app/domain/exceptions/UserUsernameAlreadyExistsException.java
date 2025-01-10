package com.fernando.ms.users.app.domain.exceptions;

public class UserUsernameAlreadyExistsException extends RuntimeException{
    public UserUsernameAlreadyExistsException(String username){
        super("User username: " + username + " already exists!");
    }
}
