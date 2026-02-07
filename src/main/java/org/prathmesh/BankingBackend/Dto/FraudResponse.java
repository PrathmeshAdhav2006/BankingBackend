package org.prathmesh.BankingBackend.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FraudResponse {

    @JsonProperty("status")
    private String status;

    @JsonProperty("fraud_score")
    private Double fraudScore;   // Use Wrapper, not primitive
}
