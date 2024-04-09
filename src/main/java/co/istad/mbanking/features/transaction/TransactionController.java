package co.istad.mbanking.features.transaction;

import co.istad.mbanking.features.transaction.dto.TransactionCreateRequest;
import co.istad.mbanking.features.transaction.dto.TransactionResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private  final  TransactionService transactionService;

//    @RequestBody (HttpStatus.ACCEPTED)
    @PostMapping
    TransactionResponse transfer(@Valid @RequestBody  TransactionCreateRequest transactionCreateRequest)
    {
        return transactionService.transfer(transactionCreateRequest);
    }

    @GetMapping
    Page<TransactionResponse> getTransactionHistory(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "25") int size,
            @RequestParam(required = false) String direction,
            @RequestParam(required = false) String transactionType
    ){
        return transactionService.findHistoryTransaction(page, size, direction, transactionType);
    }

}
