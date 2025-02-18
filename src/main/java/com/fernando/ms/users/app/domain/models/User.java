package com.fernando.ms.users.app.domain.models;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String username;
    private String names;
    private String email;
    private Boolean admin;
    private String password;
    private String newPassword;
    private String confirmPassword;
    private String passwordHash;
    private String passwordSalt;

    public User(String username,String names,String email, String password,Boolean admin){
        this.username=username;
        this.names=names;
        this.email=email;
        this.password=password;
        this.admin=admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username)&& Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username,email);
    }

}
