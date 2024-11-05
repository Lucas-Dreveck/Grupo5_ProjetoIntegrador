package com.ambientese.grupo5.Exception;

public class InvalidQuestionCountException extends RuntimeException {
    public InvalidQuestionCountException() {
        super("Número de questões inválido");
    }

    public InvalidQuestionCountException(String message, Throwable cause) {
        super(message, cause);
    }
}

