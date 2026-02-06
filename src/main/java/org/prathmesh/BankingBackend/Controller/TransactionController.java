package org.prathmesh.BankingBackend.Controller;

import org.prathmesh.BankingBackend.Dto.TransferRequest;
import org.prathmesh.BankingBackend.Dto.TransactionResponse;
import org.prathmesh.BankingBackend.Service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // =====================================================
    // USER → TRANSFER ONLY
    // =====================================================

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(
            @RequestBody TransferRequest request) {

        TransactionResponse response =
                transactionService.transfer(request);

        return ResponseEntity.ok(response);
    }
}
