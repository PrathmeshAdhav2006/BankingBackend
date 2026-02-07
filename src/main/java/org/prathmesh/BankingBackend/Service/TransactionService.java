package org.prathmesh.BankingBackend.Service;

import org.prathmesh.BankingBackend.Dto.DepositRequest;
import org.prathmesh.BankingBackend.Dto.TransactionResponse;
import org.prathmesh.BankingBackend.Dto.TransferRequest;
import org.prathmesh.BankingBackend.Dto.WithdrawRequest;
import org.springframework.security.core.Authentication;


public interface TransactionService {
    TransactionResponse transfer(TransferRequest request, Authentication auth);
    TransactionResponse deposit(DepositRequest request, Authentication auth);
    TransactionResponse withdraw(WithdrawRequest request, Authentication auth);
}
