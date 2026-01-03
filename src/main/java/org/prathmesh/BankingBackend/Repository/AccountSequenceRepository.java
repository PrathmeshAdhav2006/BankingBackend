package org.prathmesh.BankingBackend.Repository;

import org.prathmesh.BankingBackend.Models.AccountSequence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountSequenceRepository extends JpaRepository<AccountSequence, Integer> {
}