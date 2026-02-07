package org.prathmesh.BankingBackend.Repository;

import jakarta.persistence.LockModeType;
import org.prathmesh.BankingBackend.Models.Account;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findByAccountNumber(String accountNumber);

    // 🔒 LOCK ROW UNTIL TRANSACTION COMPLETES
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.accountNumber = :acc")
    Optional<Account> findByAccountNumberForUpdate(
            @Param("acc") String acc
    );


    Optional<Account> findByAccountNumberAndUserEmail(String accountNumber, String email);

}
