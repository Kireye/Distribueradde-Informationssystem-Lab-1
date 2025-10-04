package com.werkstrom.distinfolab1.db.exceptions;

public class QueryException extends RuntimeException {
    public QueryException() {
        super();
    }
    public QueryException(String message) {
    super(message);
  }
}
