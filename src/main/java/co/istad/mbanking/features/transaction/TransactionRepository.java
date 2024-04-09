package co.istad.mbanking.features.transaction;

import co.istad.mbanking.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    Page<Transaction> findByTransactionType(String transactionType, Pageable pageable);

    List<Transaction> findAll(Sort sort);
}
