package org.prathmesh.BankingBackend.Dto;

public record KycRequest(
        String aadhaarNumber,
        String panNumber,
        String aadhaarImageUrl,
        String panImageUrl
) {}
