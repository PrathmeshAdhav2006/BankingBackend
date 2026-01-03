package org.prathmesh.BankingBackend.Repository;

import org.prathmesh.BankingBackend.Models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Integer> {

    Optional<Account> findByAccountNumber(String accountNumber);
}
