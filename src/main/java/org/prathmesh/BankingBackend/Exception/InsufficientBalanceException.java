package org.prathmesh.BankingBackend.Exception;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
