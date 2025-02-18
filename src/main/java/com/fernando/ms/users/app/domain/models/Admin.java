package com.fernando.ms.users.app.domain.models;

public class Admin extends User{
    public Admin(String username, String names, String email, String password) {
        super(username,names,email,password,true);
    }
}
