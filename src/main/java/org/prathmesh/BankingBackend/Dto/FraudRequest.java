package org.prathmesh.BankingBackend.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FraudRequest {

    @JsonProperty("amount")
    private double amount;

    @JsonProperty("hour")
    private int hour;

    @JsonProperty("txn_type")
    private int txnType;

    @JsonProperty("kyc_status")
    private int kycStatus;

    @JsonProperty("daily_txn_count")
    private int dailyTxnCount;

    @JsonProperty("sender_balance")
    private double senderBalance;

    @JsonProperty("txn_gap_minutes")
    private long txnGapMinutes;

    @JsonProperty("amount_vs_avg")
    private double amountVsAvg;
}
