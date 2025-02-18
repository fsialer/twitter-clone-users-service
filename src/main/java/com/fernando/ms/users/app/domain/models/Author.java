package com.fernando.ms.users.app.domain.models;

public class Author extends User{
    public Author(String username, String names, String email, String password) {
        super(username,names,email,password,false);
    }
}
