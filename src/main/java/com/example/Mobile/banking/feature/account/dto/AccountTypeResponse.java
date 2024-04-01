package com.example.Mobile.banking.feature.account.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AccountTypeResponse(
        @NotBlank
                @Size(max=9)
        String actNo,
        String actName,
        @NotBlank
                @NotNull
                @Column(unique = true)
        String alias,

        Boolean isHidden
) {
}
