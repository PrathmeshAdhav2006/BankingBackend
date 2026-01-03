package org.prathmesh.BankingBackend.Dto;

import org.prathmesh.BankingBackend.Enums.AccountType;

public record AccountCreateRequest(
        AccountType accountType,
        Long userId
) {}
