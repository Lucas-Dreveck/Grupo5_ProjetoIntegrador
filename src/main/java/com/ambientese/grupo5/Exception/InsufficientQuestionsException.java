package com.ambientese.grupo5.exception;

public class InsufficientQuestionsException extends RuntimeException {
    public InsufficientQuestionsException() {
        super("Não foi possível encontrar o número necessário de questões");
    }

    public InsufficientQuestionsException(String message, Throwable cause) {
        super(message, cause);
    }
}