package com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordRequest {
    @NotBlank(message = "Field password cannot be null or blank")
    private String password;
    @NotBlank(message = "Field newPassword cannot be null or blank")
    private String newPassword;
    @NotBlank(message = "Field confirmPassword cannot be null or blank")
    private String confirmPassword;
}
