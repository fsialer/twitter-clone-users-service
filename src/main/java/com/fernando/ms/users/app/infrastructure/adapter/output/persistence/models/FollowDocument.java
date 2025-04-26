package com.fernando.ms.users.app.infrastructure.adapter.output.persistence.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@Document(collection = "follow")
public class FollowDocument {
    @Id
    private String id;
    private String followerId;
    private String followedId;
    private LocalDateTime createdAt;
}
