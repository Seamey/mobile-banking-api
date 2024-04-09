package co.istad.mbanking.features.transaction;

import co.istad.mbanking.domain.Account;
import co.istad.mbanking.domain.Transaction;
import co.istad.mbanking.features.account.AccountRepository;
import co.istad.mbanking.features.transaction.dto.TransactionCreateRequest;
import co.istad.mbanking.features.transaction.dto.TransactionResponse;
import co.istad.mbanking.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private  final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;

    @Transactional  // if something worng it roll back
    @Override
    public TransactionResponse transfer(TransactionCreateRequest transactionCreateRequest) {
//        log.info("transfer(TransactionCreateRequest transactionCreateRequest")

        // validate owner account no
        Account owner = accountRepository.findByActNo(transactionCreateRequest.ownerActNo())
                .orElseThrow(()->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Account owner has not been found"));

        // validate transferReceiver account no
        Account transferReceiver = accountRepository.findByActNo(transactionCreateRequest.transferReceiverActNo())
                .orElseThrow(()->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Account transfer receiver has not been found"));
        // check amount transfer( amount <= balance) (act owner only)
        if(owner.getBalance().doubleValue() < transactionCreateRequest.amount().doubleValue()){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Insufficient balance");
        }
        // check amount transfer with transfer limit
        if(transactionCreateRequest.amount().doubleValue() >= owner.getTransferLimit().doubleValue() ){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Transaction has been over the transfer limit ");
        }

        // withdrawal
         owner.setBalance(owner.getBalance().subtract(transactionCreateRequest.amount()));
        // deposit

        transferReceiver.setBalance(transferReceiver.getBalance().add(transactionCreateRequest.amount()));
        Transaction transaction = transactionMapper.formTransactionCreateRequest(transactionCreateRequest);
        transaction.setOwner(owner);
        transaction.setTransferReceiver(transferReceiver);
        transaction.setTransactionType("PAYMENT");
        transaction.setStatus(true);
        transaction.setTransactionAt(LocalDateTime.now());
        transaction = transactionRepository.save(transaction);

        return transactionMapper.toTransactionResponse(transaction);


    }

    @Override
    public Page<TransactionResponse> findHistoryTransaction(int page, int size, String direction,String transactionType) {
        if(page<0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Page mut be greater than 0");
        }
        if(size<0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Size must be greater than 0");
        }
        //  sorting direction
        Sort.Direction directionSort = Sort.Direction.ASC;
        if (direction != null && direction.equalsIgnoreCase("desc")) {
            directionSort = Sort.Direction.DESC;
        }
        // sorting by date
        Sort sort;
        if ("type".equalsIgnoreCase(transactionType)) {
            sort = Sort.by(directionSort, "transactionType");
        } else {
            sort = Sort.by(directionSort, "transactionAt");
        }

        // Create page request
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        // Fetch transactions from repository based on transaction type
        Page<Transaction> transactionsPage;
        if ("transfer".equalsIgnoreCase(transactionType)) {
            transactionsPage = transactionRepository.findByTransactionType("TRANSFER", pageRequest);
        } else if ("payment".equalsIgnoreCase(transactionType)) {
            transactionsPage = transactionRepository.findByTransactionType("PAYMENT", pageRequest);
        } else {
            transactionsPage = transactionRepository.findAll(pageRequest);
        }

        // Map transactions to TransactionResponse and return page
        return transactionsPage.map(transactionMapper::toTransactionResponse);
    }
}
