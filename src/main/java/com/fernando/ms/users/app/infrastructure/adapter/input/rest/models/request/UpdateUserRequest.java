package com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request;

import com.fernando.ms.users.app.domain.enums.TypeSex;
import com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.validation.EnumValidator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

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
    @NotBlank(message = "Field sex cannot be null or blank")
    @EnumValidator(enumClass = TypeSex.class, message = "Type sex is not valid")
    private String sex;
    @NotNull(message = "Field birth cannot be null")
    private LocalDate birth;
}
