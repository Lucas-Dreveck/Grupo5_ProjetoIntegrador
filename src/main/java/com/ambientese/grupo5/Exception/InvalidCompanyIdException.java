package com.ambientese.grupo5.Exception;

public class InvalidCompanyIdException extends RuntimeException {
    public InvalidCompanyIdException() {
        super("Empresa não encontrada");
    }

    public InvalidCompanyIdException(String message, Throwable cause) {
        super(message, cause);
    }
}