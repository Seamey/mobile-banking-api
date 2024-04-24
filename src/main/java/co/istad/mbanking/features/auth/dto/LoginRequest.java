package co.istad.mbanking.features.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Username is required")
        String phoneNumber,
        @NotBlank(message = "password is required")
        String password
) {
}
