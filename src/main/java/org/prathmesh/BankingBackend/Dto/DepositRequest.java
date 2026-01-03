package org.prathmesh.BankingBackend.Dto;

import java.math.BigDecimal;

public record DepositRequest(
        String accountNumber,
        BigDecimal amount,
        String description
) {}
