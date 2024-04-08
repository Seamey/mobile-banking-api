package co.istad.mbanking.features.account;

import co.istad.mbanking.domain.Account;
import co.istad.mbanking.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface UserAccountRepository extends JpaRepository<UserAccount,Long> {
    Optional<UserAccount> findByAccount(Account account);

}

