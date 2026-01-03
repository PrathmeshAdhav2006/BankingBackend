package org.prathmesh.BankingBackend.Service;

import org.prathmesh.BankingBackend.Dto.DepositRequest;
import org.prathmesh.BankingBackend.Dto.TransactionResponse;
import org.prathmesh.BankingBackend.Dto.TransferRequest;
import org.prathmesh.BankingBackend.Dto.WithdrawRequest;


public interface TransactionService {

    TransactionResponse transfer(TransferRequest request);

    TransactionResponse deposit(DepositRequest request);

    TransactionResponse withdraw(WithdrawRequest request);
}
