package com.fernando.ms.users.app.domain.models;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class User {
    private String id;
    private String names;
    private String lastNames;
    private String fullName;
    private String email;
    private String userId;
    private LocalDate birth;
    private String sex;
    private LocalDate createdAt;
}
