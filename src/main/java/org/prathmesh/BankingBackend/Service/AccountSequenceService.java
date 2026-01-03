package org.prathmesh.BankingBackend.Service;

import jakarta.transaction.Transactional;
import org.prathmesh.BankingBackend.Models.AccountSequence;
import org.prathmesh.BankingBackend.Repository.AccountSequenceRepository;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
public class AccountSequenceService {

    private final AccountSequenceRepository accountSequenceRepository;

    public AccountSequenceService(AccountSequenceRepository accountSequenceRepository) {
        this.accountSequenceRepository = accountSequenceRepository;
    }

    /**
     * Generates account number in format:
     * ACC + YEAR + 6-digit sequential number
     * Example: ACC2025000001
     */
    @Transactional
    public String generateAccountNumber() {

        int currentYear = Year.now().getValue();

        AccountSequence sequence = accountSequenceRepository
                .findById(currentYear)
                .orElseGet(() -> new AccountSequence(currentYear, 0));

        long nextNumber = sequence.getLastNumber() + 1;
        sequence.setLastNumber(nextNumber);

        accountSequenceRepository.save(sequence);

        return "ACC" + currentYear + String.format("%06d", nextNumber);
    }
}
