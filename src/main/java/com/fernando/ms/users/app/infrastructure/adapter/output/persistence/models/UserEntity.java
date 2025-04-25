package com.fernando.ms.users.app.infrastructure.adapter.output.persistence.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@Table
public class UserEntity {
    @Id
    private Long id;
    private String names;
    private String lastNames;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
