package org.prathmesh.BankingBackend.Controller;

import lombok.RequiredArgsConstructor;
import org.prathmesh.BankingBackend.Service.MLTestService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class MLPublicTestController {

    private final MLTestService mlTestService;

    @GetMapping("/test-ml")
    public String testML() {

        double score = mlTestService.testML();

        return "Fraud Score = " + score;
    }
}
