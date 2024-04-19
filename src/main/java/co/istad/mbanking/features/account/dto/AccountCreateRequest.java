package co.istad.mbanking.features.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.aspectj.bridge.IMessage;

import java.math.BigDecimal;

public record AccountCreateRequest(
        @NotBlank(message = "Alias is required")
        String alias,
        @NotNull(message = "First balance is required (5$ up)")
        BigDecimal balance,
        @NotBlank(message = "Account Type is required")
                // but when u use it to object is u make dto tobe public
        String accountTypeAlias,/// = relationship between account and account type but it not the object form
        String userUUid,
        String cardNumber/// if user create account type card

) {
}
