package co.istad.mbanking.features.account;

import co.istad.mbanking.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
@RestController
public interface AccountRepository extends JpaRepository<Account,Long> {

    Optional<Account> findByActNo(String actNo);
    boolean existsByActNo(String actNo);
    // that use JPQL (compiler time )
    @Modifying
    @Query("""
        UPDATE Account AS a
        SET a.isHidden = TRUE
        WHERE a.actNo =?1
""")
    void hideAccountByActNo(String actNo);

}
