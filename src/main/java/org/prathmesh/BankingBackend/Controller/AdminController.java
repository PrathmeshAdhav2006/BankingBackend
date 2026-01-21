package org.prathmesh.BankingBackend.Controller;

import lombok.RequiredArgsConstructor;
import org.prathmesh.BankingBackend.Service.KycService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final KycService kycService;

    @PostMapping("/kyc/verify/{kycId}")
    public String verifyKyc(
            @PathVariable Long kycId,
            @RequestParam boolean approve,
            @RequestParam(required = false) String reason) {

        kycService.verifyKyc(kycId, approve, reason);

        return approve
                ? "KYC approved"
                : "KYC rejected";
    }
}
