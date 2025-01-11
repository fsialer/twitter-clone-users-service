package com.fernando.ms.users.app.domain.exceptions;

public class UserEmailAlreadyExistsException extends RuntimeException{
    public UserEmailAlreadyExistsException(String email){
        super("User email: " + email + " already exists!");
    }
}
