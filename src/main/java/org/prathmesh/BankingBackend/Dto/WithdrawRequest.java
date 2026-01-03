package org.prathmesh.BankingBackend.Dto;

import java.math.BigDecimal;

public record WithdrawRequest(
        String accountNumber,
        BigDecimal amount,
        String description
) {}