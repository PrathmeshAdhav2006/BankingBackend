package org.prathmesh.BankingBackend.Controller;

import lombok.RequiredArgsConstructor;
import org.prathmesh.BankingBackend.Dto.KycVerificationRequest;
import org.prathmesh.BankingBackend.Service.KycService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminKycController {

    private final KycService kycService;

    @PostMapping("/kyc/verify/{kycId}")
    public String verifyKyc(
            @PathVariable Long kycId,
            @RequestBody KycVerificationRequest request) {

        kycService.verifyKyc(
                kycId,
                request.approve(),
                request.reason()
        );

        return request.approve()
                ? "KYC approved"
                : "KYC rejected";
    }

}
