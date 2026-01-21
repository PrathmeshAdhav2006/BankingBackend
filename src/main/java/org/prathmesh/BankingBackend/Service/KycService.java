package org.prathmesh.BankingBackend.Service;

import lombok.RequiredArgsConstructor;
import org.prathmesh.BankingBackend.Enums.KycStatus;
import org.prathmesh.BankingBackend.Models.Kyc;
import org.prathmesh.BankingBackend.Models.User;
import org.prathmesh.BankingBackend.Repository.KycRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class KycService {

    private final KycRepository kycRepository;

    public Kyc submitKyc(
            User user,
            String aadhaar,
            String pan,
            String aadhaarUrl,
            String panUrl) {

        if (kycRepository.findByUser(user).isPresent()) {
            throw new RuntimeException("KYC already submitted");
        }

        Kyc kyc = new Kyc();
        kyc.setUser(user);
        kyc.setAadhaarNumber(aadhaar);
        kyc.setPanNumber(pan);
        kyc.setAadhaarImageUrl(aadhaarUrl);
        kyc.setPanImageUrl(panUrl);
        kyc.setStatus(KycStatus.PENDING);

        return kycRepository.save(kyc);
    }



    @Transactional
    public void verifyKyc(Long kycId, boolean approve, String reason) {

        Kyc kyc = kycRepository.findById(kycId)
                .orElseThrow();

        if (approve) {
            kyc.setStatus(KycStatus.VERIFIED);
            kyc.setVerifiedAt(LocalDateTime.now());
            kyc.setRejectionReason(null);
        } else {
            kyc.setStatus(KycStatus.REJECTED);
            kyc.setRejectionReason(reason);
        }
    }

}
