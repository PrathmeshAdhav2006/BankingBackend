package org.prathmesh.BankingBackend.Service;

import lombok.RequiredArgsConstructor;
import org.prathmesh.BankingBackend.Dto.AccountCreateRequest;
import org.prathmesh.BankingBackend.Dto.AccountResponse;
import org.prathmesh.BankingBackend.Enums.AccountStatus;
import org.prathmesh.BankingBackend.Enums.Role;
import org.prathmesh.BankingBackend.Exception.*;
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
    // CREATE ACCOUNT
    // =====================================================

    @Transactional
    public AccountResponse createAccount(
            AccountCreateRequest request,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new BusinessException("User not found"));

        Account account = new Account();

        // Generate account number
        account.setAccountNumber(
                accountSequenceService.generateAccountNumber()
        );

        account.setAccountType(request.accountType());
        account.setBalance(null); // handled by @PrePersist
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setUser(user);

        Account saved = accountRepository.save(account);

        return mapToResponse(saved);
    }

    // =====================================================
    // GET ACCOUNT BY NUMBER (SECURED)
    // =====================================================

    @Transactional(readOnly = true)
    public AccountResponse getAccountByNumber(String accountNumber,
                                              String email) {

        // Get logged-in user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new BusinessException("User not found"));

        // Find account
        Account account = accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found"));

        // ADMIN can access any account
        if (user.getRole() == Role.ADMIN) {
            return mapToResponse(account);
        }

        // USER can access only own account
        if (!account.getUser().getEmail().equals(email)) {

            throw new AccessDeniedBusinessException(
                    "You are not allowed to access this account");
        }

        return mapToResponse(account);
    }

    // =====================================================
    // HELPER
    // =====================================================

    private AccountResponse mapToResponse(Account account) {

        return new AccountResponse(
                account.getAccountNumber(),
                account.getAccountType(),
                account.getBalance(),
                account.getAccountStatus(),
                account.getCreatedAt()
        );
    }
}
