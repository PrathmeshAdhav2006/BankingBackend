package org.prathmesh.BankingBackend.Dto;

import org.prathmesh.BankingBackend.Enums.KycStatus;

public record KycStatusResponse(
        KycStatus status,
        String rejectionReason
) {}
