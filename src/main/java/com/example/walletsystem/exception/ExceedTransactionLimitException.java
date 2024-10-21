package com.example.walletsystem.exception;

public class ExceedTransactionLimitException extends RuntimeException {
    //فراتر رفتن از حد استثنای معامله
    public ExceedTransactionLimitException(String message) {
        super(message);
    }
}
