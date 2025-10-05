package com.werkstrom.distinfolab1.db.exceptions;

public class NoResultException extends RuntimeException {
    public NoResultException() {
        super();
    }
    public NoResultException(String message) {
        super(message);
    }
}
