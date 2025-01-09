package com.fernando.ms.users.app.domain.models;

import lombok.*;

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
    private String passwordHash;

}
