package org.prathmesh.BankingBackend.Controller;

import org.prathmesh.BankingBackend.Dto.DepositRequest;
import org.prathmesh.BankingBackend.Dto.TransactionResponse;
import org.prathmesh.BankingBackend.Dto.TransferRequest;
import org.prathmesh.BankingBackend.Dto.WithdrawRequest;
import org.prathmesh.BankingBackend.Service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // ---------------- TRANSFER ----------------
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(
            @RequestBody TransferRequest request) {

        TransactionResponse response =
                transactionService.transfer(request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // ---------------- DEPOSIT ----------------
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(
            @RequestBody DepositRequest request) {

        TransactionResponse response =
                transactionService.deposit(request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // ---------------- WITHDRAW ----------------
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(
            @RequestBody WithdrawRequest request) {

        TransactionResponse response =
                transactionService.withdraw(request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}