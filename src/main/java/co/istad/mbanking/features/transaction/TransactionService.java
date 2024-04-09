package co.istad.mbanking.features.transaction;

import co.istad.mbanking.features.transaction.dto.TransactionCreateRequest;
import co.istad.mbanking.features.transaction.dto.TransactionResponse;
import org.springframework.data.domain.Page;

public interface TransactionService {

    TransactionResponse transfer(TransactionCreateRequest transactionCreateRequest);
    Page<TransactionResponse> findHistoryTransaction(int page, int size, String direction,String transactionType);

}
