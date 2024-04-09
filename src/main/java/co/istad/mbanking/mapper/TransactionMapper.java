package co.istad.mbanking.mapper;

import co.istad.mbanking.domain.Transaction;
import co.istad.mbanking.features.transaction.dto.TransactionCreateRequest;
import co.istad.mbanking.features.transaction.dto.TransactionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionResponse toTransactionResponse(Transaction transaction);
    Transaction formTransactionCreateRequest(TransactionCreateRequest transactionCreateRequest);
}
