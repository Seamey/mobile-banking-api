package com.example.Mobile.banking.feature.account;

import com.example.Mobile.banking.domain.Account;
import com.example.Mobile.banking.domain.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType,Integer> {
    Optional<AccountType> findByAlias(String alias);
}
