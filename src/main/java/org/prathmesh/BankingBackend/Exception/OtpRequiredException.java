package org.prathmesh.BankingBackend.Exception;

public class OtpRequiredException extends RuntimeException {

    public OtpRequiredException(String message) {
        super(message);
    }
}