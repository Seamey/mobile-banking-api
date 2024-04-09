package co.istad.mbanking.features.transaction.dto;

import co.istad.mbanking.features.account.dto.AccountSnippetResponse;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponse(
        AccountSnippetResponse owner,
        AccountSnippetResponse transferReceiver,
        BigDecimal amount,
        Boolean status,
        String remark,
        LocalDate transferAt,
        String transactionType
) {
}
