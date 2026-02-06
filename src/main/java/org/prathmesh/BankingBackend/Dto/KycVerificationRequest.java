package org.prathmesh.BankingBackend.Dto;

public record KycVerificationRequest(
        boolean approve,
        String reason
) {}
