package org.prathmesh.BankingBackend.Controller;

import lombok.RequiredArgsConstructor;
import org.prathmesh.BankingBackend.Dto.KycRequest;
import org.prathmesh.BankingBackend.Dto.KycStatusResponse;
import org.prathmesh.BankingBackend.Enums.KycStatus;
import org.prathmesh.BankingBackend.Models.Kyc;
import org.prathmesh.BankingBackend.Models.User;
import org.prathmesh.BankingBackend.Service.KycService;
import org.prathmesh.BankingBackend.Service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kyc")
@RequiredArgsConstructor
public class KycController {

    private final KycService kycService;
    private final UserService userService;

    // ============================================================
    // USER: SUBMIT KYC
    // ============================================================

    @PostMapping("/submit")
    public String submitKyc(
            @RequestBody KycRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userService.getByEmail(email);

        kycService.submitKyc(
                user,
                request.aadhaarNumber(),
                request.panNumber(),
                request.aadhaarImageUrl(),
                request.panImageUrl()
        );

        return "KYC submitted successfully. Waiting for approval.";
    }

    // ============================================================
    // USER: CHECK KYC STATUS
    // ============================================================

    @GetMapping("/status")
    public KycStatusResponse getStatus(Authentication authentication) {

        String email = authentication.getName();
        User user = userService.getByEmail(email);

        Kyc kyc = user.getKyc();

        if (kyc == null) {
            return new KycStatusResponse(
                    KycStatus.NOT_SUBMITTED,
                    null
            );
        }

        return new KycStatusResponse(
                kyc.getStatus(),
                kyc.getRejectionReason()
        );
    }

    // ============================================================
    // ADMIN: VERIFY KYC
    // ============================================================

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/verify/{kycId}")
    public String verifyKyc(
            @PathVariable Long kycId,
            @RequestParam boolean approve,
            @RequestParam(required = false) String reason) {

        kycService.verifyKyc(kycId, approve, reason);

        return approve
                ? "KYC approved successfully"
                : "KYC rejected successfully";
    }
}
