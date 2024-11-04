package com.ambientese.grupo5.Exception;

public class InvalidCompanyIdException extends RuntimeException {
    public InvalidCompanyIdException(String message) {
        super(message);
    }

    public InvalidCompanyIdException(String message, Throwable cause) {
        super(message, cause);
    }
}