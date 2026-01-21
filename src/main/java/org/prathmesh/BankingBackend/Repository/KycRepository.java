package org.prathmesh.BankingBackend.Repository;

import org.prathmesh.BankingBackend.Models.Kyc;
import org.prathmesh.BankingBackend.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KycRepository extends JpaRepository<Kyc, Long> {

    Optional<Kyc> findByUser(User user);

    boolean existsByAadhaarNumber(String aadhaarNumber);

    boolean existsByPanNumber(String panNumber);
}
