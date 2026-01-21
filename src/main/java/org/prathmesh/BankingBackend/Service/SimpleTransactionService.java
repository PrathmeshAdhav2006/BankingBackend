package org.prathmesh.BankingBackend.Service;

import lombok.RequiredArgsConstructor;
import org.prathmesh.BankingBackend.Dto.*;
import org.prathmesh.BankingBackend.Enums.*;
import org.prathmesh.BankingBackend.Models.*;
import org.prathmesh.BankingBackend.Repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SimpleTransactionService implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    // ========================= TRANSFER =========================

    @Override
    @Transactional
    public TransactionResponse transfer(TransferRequest request) {

        Account fromAccount = accountRepository
                .findByAccountNumberForUpdate(request.fromAccountNumber())
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        Account toAccount = accountRepository
                .findByAccountNumberForUpdate(request.toAccountNumber())
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        validateAccount(fromAccount);
        validateAccount(toAccount);

        validateKyc(fromAccount.getUser());

        if (fromAccount.getBalance().compareTo(request.amount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // 💰 balance update
        fromAccount.setBalance(
                fromAccount.getBalance().subtract(request.amount())
        );

        toAccount.setBalance(
                toAccount.getBalance().add(request.amount())
        );

        Transaction txn = buildTransaction(
                request.amount(),
                TransactionType.TRANSFER,
                request.description(),
                fromAccount,
                toAccount
        );

        transactionRepository.save(txn);

        return map(txn);
    }

    // ========================= DEPOSIT =========================

    @Override
    @Transactional
    public TransactionResponse deposit(DepositRequest request) {

        Account account = accountRepository
                .findByAccountNumberForUpdate(request.accountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        validateAccount(account);
        validateAmount(request.amount());

        account.setBalance(
                account.getBalance().add(request.amount())
        );

        Transaction txn = buildTransaction(
                request.amount(),
                TransactionType.DEPOSIT,
                request.description(),
                null,
                account
        );

        transactionRepository.save(txn);

        return map(txn);
    }

    // ========================= WITHDRAW =========================

    @Override
    @Transactional
    public TransactionResponse withdraw(WithdrawRequest request) {

        Account account = accountRepository
                .findByAccountNumberForUpdate(request.accountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        validateAccount(account);
        validateKyc(account.getUser());
        validateAmount(request.amount());

        if (account.getBalance().compareTo(request.amount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(
                account.getBalance().subtract(request.amount())
        );

        Transaction txn = buildTransaction(
                request.amount(),
                TransactionType.WITHDRAW,
                request.description(),
                account,
                null
        );

        transactionRepository.save(txn);

        return map(txn);
    }

    // ========================= HELPERS =========================

    private void validateAccount(Account account) {
        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("Account is not active");
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }
    }

    private void validateKyc(User user) {
        if (user.getKyc() == null ||
                user.getKyc().getStatus() != KycStatus.VERIFIED) {
            throw new RuntimeException("KYC not verified");
        }
    }

    private Transaction buildTransaction(
            BigDecimal amount,
            TransactionType type,
            String description,
            Account from,
            Account to) {

        Transaction txn = new Transaction();
        txn.setTransactionId("TXN-" +
                UUID.randomUUID().toString().substring(0, 10).toUpperCase());
        txn.setAmount(amount);
        txn.setType(type);
        txn.setStatus(TransactionStatus.SUCCESS);
        txn.setDescription(description);
        txn.setFromAccount(from);
        txn.setToAccount(to);
        txn.setTimestamp(LocalDateTime.now());
        return txn;
    }

    private TransactionResponse map(Transaction t) {
        return new TransactionResponse(
                t.getTransactionId(),
                t.getAmount(),
                t.getType(),
                t.getStatus(),
                t.getTimestamp(),
                t.getFromAccount() != null
                        ? t.getFromAccount().getAccountNumber()
                        : null,
                t.getToAccount() != null
                        ? t.getToAccount().getAccountNumber()
                        : null,
                t.getDescription()
        );
    }
}
