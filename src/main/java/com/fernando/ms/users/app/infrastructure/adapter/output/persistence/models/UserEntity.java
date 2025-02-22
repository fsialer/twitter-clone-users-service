package com.fernando.ms.users.app.infrastructure.adapter.output.persistence.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class UserEntity {
    @Id
    private Long id;
    private String username;
    private String names;
    private String email;
    private Boolean admin;
    private String password;
    private String passwordHash;
    private String passwordSalt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Override
    public int hashCode() {
        return Objects.hash(id, username,email);
    }
}
