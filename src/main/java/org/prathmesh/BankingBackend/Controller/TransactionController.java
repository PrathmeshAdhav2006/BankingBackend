package org.prathmesh.BankingBackend.Controller;

import org.prathmesh.BankingBackend.Dto.*;
import org.prathmesh.BankingBackend.Service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(
            @RequestBody TransferRequest request) {

        return ResponseEntity.ok(
                transactionService.transfer(request)
        );
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(
            @RequestBody DepositRequest request) {

        return ResponseEntity.ok(
                transactionService.deposit(request)
        );
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(
            @RequestBody WithdrawRequest request) {

        return ResponseEntity.ok(
                transactionService.withdraw(request)
        );
    }
}
