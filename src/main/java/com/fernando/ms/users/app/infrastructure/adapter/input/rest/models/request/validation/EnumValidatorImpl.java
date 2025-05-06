package com.fernando.ms.users.app.infrastructure.adapter.input.rest.models.request.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidatorImpl implements ConstraintValidator<EnumValidator, String> {
    private EnumValidator annotation;

    @Override
    public void initialize(EnumValidator constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true; // Maneja @NotNull por separado si es necesario

        Class<? extends Enum<?>> enumClass = annotation.enumClass();
        for (Enum<?> enumVal : enumClass.getEnumConstants()) {
            if (enumVal.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
