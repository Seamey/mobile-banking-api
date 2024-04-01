package com.example.Mobile.banking.feature.user.dto;

import java.time.LocalDate;

public record UserUpdateRequest(
        String name,
        String gender,
        LocalDate dob,
        String studentIdCard

) {
}
