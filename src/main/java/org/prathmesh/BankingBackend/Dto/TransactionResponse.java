package org.prathmesh.BankingBackend.Dto;

import org.prathmesh.BankingBackend.Enums.TransactionStatus;
import org.prathmesh.BankingBackend.Enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        String transactionId,
        BigDecimal amount,
        TransactionType type,
        TransactionStatus status,
        LocalDateTime timestamp,
        String fromAccountNumber,
        String toAccountNumber,
        String description
) {}
