package org.prathmesh.BankingBackend.Controller;

import org.prathmesh.BankingBackend.Dto.TransferRequest;
import org.prathmesh.BankingBackend.Dto.TransactionResponse;
import org.prathmesh.BankingBackend.Service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // =====================================================
    // USER & ADMIN → TRANSFER (Users: own accounts only)
    // =====================================================

    @PostMapping("/transfer")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TransactionResponse> transfer(
            @RequestBody TransferRequest request,
            Authentication authentication) {

        TransactionResponse response =
                transactionService.transfer(request, authentication);

        return ResponseEntity.ok(response);
    }
}
