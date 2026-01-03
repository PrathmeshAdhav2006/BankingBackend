package org.prathmesh.BankingBackend.Dto;

import org.prathmesh.BankingBackend.Enums.AccountStatus;
import org.prathmesh.BankingBackend.Enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountResponse(
        String accountNumber,
        AccountType accountType,
        BigDecimal balance,
        AccountStatus status,
        LocalDateTime createdAt
) {}
