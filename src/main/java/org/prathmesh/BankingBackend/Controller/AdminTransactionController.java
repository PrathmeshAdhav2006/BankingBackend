package org.prathmesh.BankingBackend.Controller;

import lombok.RequiredArgsConstructor;
import org.prathmesh.BankingBackend.Dto.DepositRequest;
import org.prathmesh.BankingBackend.Dto.TransactionResponse;
import org.prathmesh.BankingBackend.Dto.WithdrawRequest;
import org.prathmesh.BankingBackend.Service.TransactionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/transactions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminTransactionController {

    private final TransactionService transactionService;

    // =====================================================
    // ADMIN → CASH DEPOSIT
    // =====================================================

    @PostMapping("/deposit")
    public TransactionResponse deposit(
            @RequestBody DepositRequest request) {

        return transactionService.deposit(request);
    }

    // =====================================================
    // ADMIN → CASH WITHDRAW
    // =====================================================

    @PostMapping("/withdraw")
    public TransactionResponse withdraw(
            @RequestBody WithdrawRequest request) {

        return transactionService.withdraw(request);
    }
}
