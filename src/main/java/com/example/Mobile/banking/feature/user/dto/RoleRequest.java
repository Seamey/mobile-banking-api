package com.example.Mobile.banking.feature.user.dto;

import jakarta.validation.constraints.NotBlank;

public record RoleRequest(
        @NotBlank
        String name
) {
}
