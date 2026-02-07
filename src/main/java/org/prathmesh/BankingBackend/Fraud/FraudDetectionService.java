package org.prathmesh.BankingBackend.Fraud;

import lombok.RequiredArgsConstructor;
import org.prathmesh.BankingBackend.Dto.FraudRequest;
import org.prathmesh.BankingBackend.Dto.FraudResponse;
import org.prathmesh.BankingBackend.Exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class FraudDetectionService {

    private final RestTemplate restTemplate;


    private static final String ML_URL =
            "http://127.0.0.1:8000/predict";


    public double getFraudScore(FraudRequest request) {

        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<FraudRequest> entity =
                    new HttpEntity<>(request, headers);

            ResponseEntity<FraudResponse> response =
                    restTemplate.postForEntity(
                            ML_URL,
                            entity,
                            FraudResponse.class
                    );

            if (response.getBody() == null) {
                throw new BusinessException(
                        "Fraud engine returned empty response"
                );
            }

            return response.getBody().getFraudScore();

        }
        catch (RestClientException ex) {

            // ML service down / timeout / network error
            throw new BusinessException(
                    "Fraud engine unavailable. Try again later."
            );

        }
        catch (Exception ex) {

            // Any unexpected error
            throw new BusinessException(
                    "Fraud check failed"
            );
        }
    }
}
