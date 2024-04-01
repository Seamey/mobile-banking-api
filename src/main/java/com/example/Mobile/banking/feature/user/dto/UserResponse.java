package com.example.Mobile.banking.feature.user.dto;

import java.time.LocalDate;

public record UserResponse(
        String uuid,
        String name,
        String profileImage,
        String gender,
        LocalDate dob


) {
}
