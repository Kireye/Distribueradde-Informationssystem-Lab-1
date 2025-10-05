package com.werkstrom.distinfolab1.db.exceptions;

public class TransactionException extends RuntimeException {
    public TransactionException() {
        super();
    }
    public TransactionException(String message) {
        super(message);
    }
}
