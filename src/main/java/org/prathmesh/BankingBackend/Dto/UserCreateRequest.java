package org.prathmesh.BankingBackend.Dto;

public record UserCreateRequest(
        String fullName,
        String email,
        String password
){}
