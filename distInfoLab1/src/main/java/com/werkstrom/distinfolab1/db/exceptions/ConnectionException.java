package com.werkstrom.distinfolab1.db.exceptions;

public class ConnectionException extends RuntimeException {
    public ConnectionException() {
        super();
    }
    public ConnectionException(String message) {
        super(message);
    }
}
