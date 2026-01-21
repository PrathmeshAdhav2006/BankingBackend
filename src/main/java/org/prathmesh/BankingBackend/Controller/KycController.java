package org.prathmesh.BankingBackend.Controller;

import lombok.RequiredArgsConstructor;
import org.prathmesh.BankingBackend.Dto.KycRequest;
import org.prathmesh.BankingBackend.Dto.KycStatusResponse;
import org.prathmesh.BankingBackend.Enums.KycStatus;
import org.prathmesh.BankingBackend.Models.Kyc;
import org.prathmesh.BankingBackend.Models.User;
import org.prathmesh.BankingBackend.Service.KycService;
import org.prathmesh.BankingBackend.Service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kyc")
@RequiredArgsConstructor
public class KycController {

    private final KycService kycService;
    private final UserService userService;

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

        return "KYC submitted successfully";
    }

    @GetMapping("/status")
    public KycStatusResponse status(Authentication authentication) {

        User user = userService.getByEmail(authentication.getName());

        if (user.getKyc() == null) {
            return new KycStatusResponse(KycStatus.NOT_SUBMITTED, null);
        }

        return new KycStatusResponse(
                user.getKyc().getStatus(),
                user.getKyc().getRejectionReason()
        );
    }
}
