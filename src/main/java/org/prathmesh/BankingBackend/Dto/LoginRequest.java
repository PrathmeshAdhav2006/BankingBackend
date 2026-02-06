package org.prathmesh.BankingBackend.Dto;

public record LoginRequest(
        String email,
        String password
) {}
