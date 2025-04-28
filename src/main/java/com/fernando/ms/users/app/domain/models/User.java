package com.fernando.ms.users.app.domain.models;

import lombok.*;

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
    private String email;
    private String userId;
}
