package org.prathmesh.BankingBackend.Service;

import lombok.RequiredArgsConstructor;
import org.prathmesh.BankingBackend.Dto.*;
import org.prathmesh.BankingBackend.Enums.KycStatus;
import org.prathmesh.BankingBackend.Enums.TransactionType;
import org.prathmesh.BankingBackend.Exception.BusinessException;
import org.prathmesh.BankingBackend.Exception.OtpRequiredException;
import org.prathmesh.BankingBackend.Fraud.FraudDetectionService;
import org.prathmesh.BankingBackend.Models.Account;
import org.prathmesh.BankingBackend.Models.User;
import org.prathmesh.BankingBackend.Repository.AccountRepository;
import org.prathmesh.BankingBackend.Repository.TransactionRepository;
import org.prathmesh.BankingBackend.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Primary
@Service
@RequiredArgsConstructor
public class FraudAwareTransactionService implements TransactionService {

    private static final Logger log =
            LoggerFactory.getLogger(FraudAwareTransactionService.class);

    private final SimpleTransactionService simpleService;
    private final FraudDetectionService fraudService;

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;


    // ================= TRANSFER =================

    @Override
    @Transactional
    public TransactionResponse transfer(
            TransferRequest request,
            Authentication auth) {

        log.info("Transfer Request: {}", request);

        User user = getCurrentUser(auth);

        Account fromAccount =
                getAccount(request.fromAccountNumber());

        runFraudCheck(
                request.amount(),
                user,
                TransactionType.TRANSFER,
                fromAccount
        );

        return simpleService.transfer(request, auth);
    }


    // ================= DEPOSIT =================

    @Override
    @Transactional
    public TransactionResponse deposit(
            DepositRequest request,
            Authentication auth) {

        log.info("Deposit Request: {}", request);

        User user = getCurrentUser(auth);

        Account account =
                getAccount(request.accountNumber());

        runFraudCheck(
                request.amount(),
                user,
                TransactionType.DEPOSIT,
                account
        );

        return simpleService.deposit(request, auth);
    }


    // ================= WITHDRAW =================

    @Override
    @Transactional
    public TransactionResponse withdraw(
            WithdrawRequest request,
            Authentication auth) {

        log.info("Withdraw Request: {}", request);

        User user = getCurrentUser(auth);

        Account account =
                getAccount(request.accountNumber());

        runFraudCheck(
                request.amount(),
                user,
                TransactionType.WITHDRAW,
                account
        );

        return simpleService.withdraw(request, auth);
    }


    // ================= FRAUD CHECK =================

    private void runFraudCheck(
            BigDecimal amount,
            User user,
            TransactionType type,
            Account account
    ) {

        FraudRequest fraudRequest =
                buildFraudRequest(amount, user, type, account);

        log.info("Fraud Request = {}", fraudRequest);

        double score =
                fraudService.getFraudScore(fraudRequest);

        log.info("Fraud Score = {}", score);


        // ✅ LOW RISK → ALLOW
        if (score < 25) {

            log.info("APPROVED: Low Risk (Score = {})", score);
            return;
        }


        // ⚠️ MEDIUM RISK → OTP
        if (score < 75) {

            log.warn("OTP REQUIRED: Medium Risk (Score = {})", score);

            throw new OtpRequiredException(
                    "OTP verification required (Fraud Score = " + score + ")"
            );
        }


        // ❌ HIGH RISK → BLOCK
        log.error("BLOCKED: High Risk (Score = {})", score);

        throw new BusinessException(
                "Transaction blocked due to high fraud risk (Score = " + score + ")"
        );
    }


    // ================= FEATURE BUILDER =================

    private FraudRequest buildFraudRequest(
            BigDecimal amount,
            User user,
            TransactionType type,
            Account account
    ) {

        FraudRequest req = new FraudRequest();


        // 1️⃣ Amount
        double txnAmount = amount.doubleValue();
        req.setAmount(txnAmount);


        // 2️⃣ Hour
        req.setHour(LocalDateTime.now().getHour());


        // 3️⃣ Transaction Type
        req.setTxnType(type.ordinal());


        // 4️⃣ KYC Status
        int kyc =
                (user.getKyc() != null &&
                        user.getKyc().getStatus() == KycStatus.VERIFIED)
                        ? 1 : 0;

        req.setKycStatus(kyc);


        // 5️⃣ Today's Transaction Count
        LocalDateTime startOfDay =
                LocalDateTime.now()
                        .toLocalDate()
                        .atStartOfDay();

        LocalDateTime endOfDay =
                startOfDay
                        .plusDays(1)
                        .minusSeconds(1);

        int todayCount =
                transactionRepository
                        .countTodayTransactions(
                                user.getId(),
                                startOfDay,
                                endOfDay
                        );

        req.setDailyTxnCount(todayCount);


        // 6️⃣ Sender Balance
        req.setSenderBalance(
                account.getBalance().doubleValue()
        );


        // 7️⃣ Gap From Last Transaction
        LocalDateTime lastTxn =
                transactionRepository
                        .findLastTransactionTime(user.getId());

        long gapMinutes = 0;

        if (lastTxn != null) {

            gapMinutes = Duration.between(
                    lastTxn,
                    LocalDateTime.now()
            ).toMinutes();
        }

        req.setTxnGapMinutes(gapMinutes);


        // 8️⃣ Amount vs Average
        Double avg =
                transactionRepository
                        .findAverageAmount(user.getId());

        double avgAmount =
                (avg == null || avg == 0)
                        ? txnAmount
                        : avg;

        double ratio = txnAmount / avgAmount;

        req.setAmountVsAvg(ratio);


        return req;
    }


    // ================= HELPERS =================

    private User getCurrentUser(Authentication auth) {

        return userRepository
                .findByEmail(auth.getName())
                .orElseThrow(() ->
                        new BusinessException("User not found"));
    }


    private Account getAccount(String accountNo) {

        return accountRepository
                .findByAccountNumberForUpdate(accountNo)
                .orElseThrow(() ->
                        new BusinessException("Account not found"));
    }
}
