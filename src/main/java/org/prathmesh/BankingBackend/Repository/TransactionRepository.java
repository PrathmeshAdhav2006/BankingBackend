package org.prathmesh.BankingBackend.Repository;

import org.prathmesh.BankingBackend.Models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
}
