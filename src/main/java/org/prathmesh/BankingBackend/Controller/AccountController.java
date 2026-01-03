package org.prathmesh.BankingBackend.Controller;

import org.prathmesh.BankingBackend.Dto.AccountCreateRequest;
import org.prathmesh.BankingBackend.Dto.AccountResponse;
import org.prathmesh.BankingBackend.Service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountCreateRequest accountRequestDto){
        AccountResponse savedAccount = accountService.createAccount(accountRequestDto);
        return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable String accountNumber){
        AccountResponse account = accountService.getAccountByNumber(accountNumber);
        return new ResponseEntity<>(account,HttpStatus.OK);
    }

}
