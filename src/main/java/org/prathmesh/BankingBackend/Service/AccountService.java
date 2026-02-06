package org.prathmesh.BankingBackend.Service;

import lombok.RequiredArgsConstructor;
import org.prathmesh.BankingBackend.Dto.AccountCreateRequest;
import org.prathmesh.BankingBackend.Dto.AccountResponse;
import org.prathmesh.BankingBackend.Enums.AccountStatus;
import org.prathmesh.BankingBackend.Models.Account;
import org.prathmesh.BankingBackend.Models.User;
import org.prathmesh.BankingBackend.Repository.AccountRepository;
import org.prathmesh.BankingBackend.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountSequenceService accountSequenceService;

    // =====================================================
    // CREATE ACCOUNT (JWT BASED)
    // =====================================================

    @Transactional
    public AccountResponse createAccount(
            AccountCreateRequest request,
            String email) {

        // 🔐 get logged-in user from JWT email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Account account = new Account();

        // generate account number
        account.setAccountNumber(
                accountSequenceService.generateAccountNumber()
        );

        account.setAccountType(request.accountType());
        account.setBalance(null); // auto handled by @PrePersist
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setUser(user);

        Account saved = accountRepository.save(account);

        return new AccountResponse(
                saved.getAccountNumber(),
                saved.getAccountType(),
                saved.getBalance(),
                saved.getAccountStatus(),
                saved.getCreatedAt()
        );
    }

    // =====================================================
    // GET ACCOUNT BY NUMBER
    // =====================================================

    @Transactional(readOnly = true)
    public AccountResponse getAccountByNumber(String accountNumber) {

        Account account = accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));

        return new AccountResponse(
                account.getAccountNumber(),
                account.getAccountType(),
                account.getBalance(),
                account.getAccountStatus(),
                account.getCreatedAt()
        );
    }
}
