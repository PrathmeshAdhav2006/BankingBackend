package org.prathmesh.BankingBackend.Controller;

import lombok.RequiredArgsConstructor;
import org.prathmesh.BankingBackend.Dto.DepositRequest;
import org.prathmesh.BankingBackend.Dto.TransferRequest;
import org.prathmesh.BankingBackend.Dto.TransactionResponse;
import org.prathmesh.BankingBackend.Dto.WithdrawRequest;
import org.prathmesh.BankingBackend.Service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/transactions")
@RequiredArgsConstructor
public class AdminTransactionController {  // ✅ NO @PreAuthorize

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(
            @RequestBody TransferRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(transactionService.transfer(request, authentication));
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(
            @RequestBody DepositRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(transactionService.deposit(request, authentication));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(  // ✅ Changed back to TransactionResponse
                                                          @RequestBody WithdrawRequest request,
                                                          Authentication authentication) {
        return ResponseEntity.ok(transactionService.withdraw(request, authentication));
    }

    // TEMP DEBUG ENDPOINT - REMOVE AFTER TESTING
    @PostMapping("/withdrawDebug")
    public ResponseEntity<String> withdrawDebug(
            @RequestBody WithdrawRequest request,
            Authentication authentication) {
        System.out.println("🎉 ENDPOINT REACHED!");
        System.out.println("📧 Auth: " + (authentication != null ? authentication.getName() : "NULL"));
        System.out.println("🏦 Account: " + request.accountNumber());
        return ResponseEntity.ok("Account not found - " + request.accountNumber());
    }
}
