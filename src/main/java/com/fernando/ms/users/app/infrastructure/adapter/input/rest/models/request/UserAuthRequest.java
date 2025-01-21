package com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class UserAuthRequest {
    @NotBlank(message = "Field username cannot be null or blank")
    private String username;
    @NotBlank(message = "Field password cannot be null or blank")
    private String password;
}
