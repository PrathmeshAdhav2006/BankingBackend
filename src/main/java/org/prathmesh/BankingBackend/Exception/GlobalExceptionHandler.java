package org.prathmesh.BankingBackend.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // =========================
    // 404 - Not Found
    // =========================

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleAccountNotFound(
            AccountNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    // =========================
    // 403 - Forbidden
    // =========================

    @ExceptionHandler(AccessDeniedBusinessException.class)
    public ResponseEntity<String> handleAccessDenied(
            AccessDeniedBusinessException ex) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ex.getMessage());
    }

    // =========================
    // 400 - Bad Request
    // =========================

    // ❗ REMOVED BusinessException from here
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<String> handleBalance(
            InsufficientBalanceException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    // =========================
    // OTP Required
    // =========================

    @ExceptionHandler(OtpRequiredException.class)
    public ResponseEntity<?> handleOtp(OtpRequiredException ex) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(Map.of(
                        "status", "OTP_REQUIRED",
                        "message", ex.getMessage()
                ));
    }

    // =========================
    // Business Errors
    // =========================

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusiness(BusinessException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "status", "FAILED",
                        "message", ex.getMessage()
                ));
    }

    // =========================
    // 500 - Server Error
    // =========================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneral(
            Exception ex) {

        ex.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal server error");
    }
}
