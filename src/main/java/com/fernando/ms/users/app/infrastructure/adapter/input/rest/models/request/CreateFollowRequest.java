package com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class CreateFollowRequest {
    private String followedId;
}
