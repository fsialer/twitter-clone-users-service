package com.fernando.ms.users.app.domain.models;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class User {
    private Long id;
    private String names;
    private String lastNames;
    private String email;
}
