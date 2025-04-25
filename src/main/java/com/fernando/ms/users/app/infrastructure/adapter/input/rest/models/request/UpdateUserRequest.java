package com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest {
    @NotBlank(message = "Field names cannot be null or blank")
    private String names;
    @NotBlank(message = "Field lastNames cannot be null or blank")
    private String lastNames;
    @NotBlank(message = "Field email cannot be null or blank")
    @Email(message="Field email must be a valid")
    private String email;
}
