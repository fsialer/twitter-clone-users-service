package com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String names;
    private String email;
}
