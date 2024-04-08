package co.istad.mbanking.features.account.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AccountUpdateTransferLimitRequest(
        @NotNull(message = "transferLimit is required")
        BigDecimal transferLimit
) {
}
