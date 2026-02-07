package org.prathmesh.BankingBackend.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
// 400 - Bad Request
    @ExceptionHandler({
            BusinessException.class,
            InsufficientBalanceException.class
    })
    public ResponseEntity<String> handleBusinessErrors(
            RuntimeException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
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
