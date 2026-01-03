package org.prathmesh.BankingBackend.Service;

import org.prathmesh.BankingBackend.Dto.DepositRequest;
import org.prathmesh.BankingBackend.Dto.TransactionResponse;
import org.prathmesh.BankingBackend.Dto.TransferRequest;
import org.prathmesh.BankingBackend.Dto.WithdrawRequest;
import org.prathmesh.BankingBackend.Enums.AccountStatus;
import org.prathmesh.BankingBackend.Enums.TransactionStatus;
import org.prathmesh.BankingBackend.Enums.TransactionType;
import org.prathmesh.BankingBackend.Models.Account;
import org.prathmesh.BankingBackend.Models.Transaction;
import org.prathmesh.BankingBackend.Repository.AccountRepository;
import org.prathmesh.BankingBackend.Repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class SimpleTransactionService implements TransactionService{

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public SimpleTransactionService(TransactionRepository transactionRepository ,
                                   AccountRepository accountRepository)
    {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository ;
    }

    @Override
    @Transactional
    public TransactionResponse transfer(TransferRequest request){

        Account fromAccount = accountRepository
                .findByAccountNumber(request.fromAccountNumber())
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        Account toAccount = accountRepository
                .findByAccountNumber(request.toAccountNumber())
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        if (fromAccount.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("Sender account is not active");
        }

        if (fromAccount.getBalance().compareTo(request.amount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        fromAccount.setBalance(
                fromAccount.getBalance().subtract(request.amount())
        );

        toAccount.setBalance(
                toAccount.getBalance().add(request.amount())
        );


        Transaction transaction = new Transaction();
        transaction.setTransactionId(generateTransactionId());
        transaction.setAmount(request.amount());
        transaction.setType(TransactionType.TRANSFER);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setDescription(request.description());
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);

        // 5️⃣ Persist
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        Transaction savedTransaction = transactionRepository.save(transaction);

        // 6️⃣ Return DTO
        return mapToResponseDto(savedTransaction);
    }

    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    private TransactionResponse mapToResponseDto(Transaction transaction) {
        return new TransactionResponse(
                transaction.getTransactionId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getStatus(),
                transaction.getTimestamp(),
                transaction.getFromAccount().getAccountNumber(),
                transaction.getToAccount().getAccountNumber(),
                transaction.getDescription()
        );
    }


    @Override
    @Transactional
    public TransactionResponse deposit(DepositRequest request) {

        //  Fetch account
        Account account = accountRepository
                .findByAccountNumber(request.accountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        //  Validate account status
        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("Account is not active");
        }

        //  Validate amount
        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Deposit amount must be greater than zero");
        }

        //  Add balance
        account.setBalance(
                account.getBalance().add(request.amount())
        );

        //  Create transaction
        Transaction transaction = new Transaction();
        transaction.setTransactionId(generateTransactionId());
        transaction.setAmount(request.amount());
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setDescription(request.description());

        // Deposit → only TO account
        transaction.setFromAccount(null);
        transaction.setToAccount(account);

        //  Persist
        accountRepository.save(account);
        Transaction savedTransaction = transactionRepository.save(transaction);

        //  Return response DTO
        return new TransactionResponse(
                savedTransaction.getTransactionId(),
                savedTransaction.getAmount(),
                savedTransaction.getType(),
                savedTransaction.getStatus(),
                savedTransaction.getTimestamp(),
                null, // fromAccountNumber (deposit)
                savedTransaction.getToAccount().getAccountNumber(),
                savedTransaction.getDescription()
        );
    }

    @Override
    @Transactional
    public TransactionResponse withdraw(WithdrawRequest request) {

        //  Fetch account
        Account account = accountRepository
                .findByAccountNumber(request.accountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        //  Validate account status
        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("Account is not active");
        }

        //  Validate amount
        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Withdraw amount must be greater than zero");
        }

        //  Check balance
        if (account.getBalance().compareTo(request.amount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        //  Subtract balance
        account.setBalance(
                account.getBalance().subtract(request.amount())
        );

        //  Create transaction
        Transaction transaction = new Transaction();
        transaction.setTransactionId(generateTransactionId());
        transaction.setAmount(request.amount());
        transaction.setType(TransactionType.WITHDRAW);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setDescription(request.description());

        // Withdraw → only FROM account
        transaction.setFromAccount(account);
        transaction.setToAccount(null);

        //  Persist
        accountRepository.save(account);
        Transaction savedTransaction = transactionRepository.save(transaction);

        //  Return response DTO
        return new TransactionResponse(
                savedTransaction.getTransactionId(),
                savedTransaction.getAmount(),
                savedTransaction.getType(),
                savedTransaction.getStatus(),
                savedTransaction.getTimestamp(),
                savedTransaction.getFromAccount().getAccountNumber(),
                null, // toAccountNumber
                savedTransaction.getDescription()
        );
    }

}
