package com.fernando.ms.users.app.infrastructure.adapter.output.persistence.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@Document(collection = "users")
public class UserEntity {
    @Id
    private String id;
    private String names;
    private String lastNames;
    @Indexed
    private String email;
    private String userId;
    private LocalDate birth;
    private String sex;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
