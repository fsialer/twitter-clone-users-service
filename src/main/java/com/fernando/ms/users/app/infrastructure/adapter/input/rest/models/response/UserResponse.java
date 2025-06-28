package com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.response;

import lombok.*;

import java.time.LocalDate;

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
    private String fullName;
    private String email;
    private String sex;
    private LocalDate birth;
    private String userId;
    private LocalDate createdAt;
}
