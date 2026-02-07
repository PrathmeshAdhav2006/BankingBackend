package org.prathmesh.BankingBackend.Exception;

public class AccessDeniedBusinessException extends RuntimeException {

    public AccessDeniedBusinessException(String message) {
        super(message);
    }
}
