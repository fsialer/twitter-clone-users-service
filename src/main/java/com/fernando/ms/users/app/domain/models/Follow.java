package com.fernando.ms.users.app.domain.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class Follow {
    private String id;
    private String followerId;
    private String followedId;
}
