package com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class UserResponse {
    private String id;
    private String names;
    private String lastNames;
    private String email;
}
