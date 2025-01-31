package com.fernando.ms.users.app.domain.models;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Long id;
    private String username;
    private String names;
    private String email;
    private String password;
    private String newPassword;
    private String confirmPassword;
    private String passwordHash;
    private String passwordSalt;

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
