package org.prathmesh.BankingBackend.Controller;

import org.prathmesh.BankingBackend.Dto.AccountCreateRequest;
import org.prathmesh.BankingBackend.Dto.AccountResponse;
import org.prathmesh.BankingBackend.Service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    // =====================================================
    // CREATE ACCOUNT (JWT REQUIRED)
    // =====================================================

    @PostMapping("/create")
    public ResponseEntity<AccountResponse> createAccount(
            @RequestBody AccountCreateRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        AccountResponse savedAccount =
                accountService.createAccount(request, email);

        return new ResponseEntity<>(
                savedAccount,
                HttpStatus.CREATED
        );
    }

    // =====================================================
    // GET ACCOUNT BY NUMBER
    // =====================================================

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccount(
            @PathVariable String accountNumber,
            Authentication authentication) { // Get JWT principal

        String email = authentication.getName(); // JWT email claim
        AccountResponse account = accountService.getAccountByNumber(accountNumber, email);

        return ResponseEntity.ok(account);
    }
}
