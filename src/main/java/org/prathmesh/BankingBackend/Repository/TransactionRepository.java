package org.prathmesh.BankingBackend.Repository;

import org.prathmesh.BankingBackend.Models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    // Count today's outgoing transactions
    @Query("""
        SELECT COUNT(t)
        FROM Transaction t
        WHERE t.fromAccount.user.id = :userId
        AND t.timestamp BETWEEN :start AND :end
    """)
    int countTodayTransactions(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );


    // Last outgoing transaction time
    @Query("""
        SELECT MAX(t.timestamp)
        FROM Transaction t
        WHERE t.fromAccount.user.id = :userId
    """)
    LocalDateTime findLastTransactionTime(
            @Param("userId") Long userId
    );


    // Average outgoing amount
    @Query("""
        SELECT AVG(t.amount)
        FROM Transaction t
        WHERE t.fromAccount.user.id = :userId
    """)
    Double findAverageAmount(
            @Param("userId") Long userId
    );
}
