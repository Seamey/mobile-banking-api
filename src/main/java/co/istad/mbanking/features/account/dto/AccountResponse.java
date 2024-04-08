package co.istad.mbanking.features.account.dto;

import co.istad.mbanking.domain.User;
import co.istad.mbanking.features.user.dto.UserResponse;

public record AccountResponse(
        String alias,
        UserResponse userResponse
) {
}
