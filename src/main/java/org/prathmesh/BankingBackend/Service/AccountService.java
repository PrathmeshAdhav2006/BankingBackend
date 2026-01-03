package org.prathmesh.BankingBackend.Service;


import org.prathmesh.BankingBackend.Dto.AccountCreateRequest;
import org.prathmesh.BankingBackend.Dto.AccountResponse;
import org.prathmesh.BankingBackend.Models.Account;
import org.prathmesh.BankingBackend.Models.User;
import org.prathmesh.BankingBackend.Repository.AccountRepository;
import org.prathmesh.BankingBackend.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AccountService {


    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountSequenceService accountSequenceService;
    public AccountService(AccountRepository accountRepository,
                          UserRepository userRepository,
                          AccountSequenceService accountSequenceService)
    {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.accountSequenceService = accountSequenceService;
    }


    @Transactional
    public AccountResponse createAccount(AccountCreateRequest accountCreateDto) {

        Optional<User> optional = userRepository.findById(accountCreateDto.userId());
        if(optional.isEmpty()){
            throw new RuntimeException("User Not Found");
        }

        User user = optional.get();
        Account account = new Account();

        String accountNumber = accountSequenceService.generateAccountNumber();
        account.setAccountNumber(accountNumber);
        account.setAccountType(accountCreateDto.accountType());
        account.setUser(user);

        Account savedAccount = accountRepository.save(account);

        return new AccountResponse(
                savedAccount.getAccountNumber(),
                savedAccount.getAccountType(),
                savedAccount.getBalance(),
                savedAccount.getAccountStatus(),
                savedAccount.getCreatedAt()
        );

    }

    public AccountResponse getAccountByNumber(String accountNumber) {

        Optional<Account> optional = accountRepository.findByAccountNumber(accountNumber);
        if(optional.isEmpty()){
            throw new RuntimeException("Account Not Exists");
        }

        Account account = optional.get();

        return new AccountResponse(
                account.getAccountNumber(),
                account.getAccountType(),
                account.getBalance(),
                account.getAccountStatus(),
                account.getCreatedAt()
        );

    }
}
