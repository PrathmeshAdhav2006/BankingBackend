package org.prathmesh.BankingBackend.Service;

import lombok.RequiredArgsConstructor;
import org.prathmesh.BankingBackend.Dto.FraudRequest;
import org.prathmesh.BankingBackend.Dto.FraudResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class MLTestService {

    private static final Logger log =
            LoggerFactory.getLogger(MLTestService.class);

    private final RestTemplate restTemplate;

    private static final String ML_URL =
            "http://127.0.0.1:8000/predict";

    public double testML() {

        // ✅ Hardcoded Request
        FraudRequest req = new FraudRequest();

        req.setAmount(100000);
        req.setHour(19);
        req.setTxnType(2);
        req.setKycStatus(0);
        req.setDailyTxnCount(500);
        req.setSenderBalance(160000);
        req.setTxnGapMinutes(0);
        req.setAmountVsAvg(500);

        log.info("Sending to ML: {}", req);

        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<FraudRequest> entity =
                    new HttpEntity<>(req, headers);

            // Call ML
            ResponseEntity<FraudResponse> response =
                    restTemplate.postForEntity(
                            ML_URL,
                            entity,
                            FraudResponse.class
                    );

            log.info("HTTP Status = {}", response.getStatusCode());
            log.info("Raw Response = {}", response);

            FraudResponse body = response.getBody();

            // ✅ Safety Check
            if (body == null) {

                log.error("ML returned NULL body");
                return -1;
            }

            log.info("ML Status = {}", body.getStatus());
            log.info("Fraud Score = {}", body.getFraudScore());

            // ✅ Handle ML Error
            if (!"success".equalsIgnoreCase(body.getStatus())
                    || body.getFraudScore() == null) {

                log.error("ML Error Response: {}", body);

                return -1;
            }

            return body.getFraudScore();

        } catch (Exception e) {

            log.error("ML call failed", e);

            return -1;
        }
    }
}
