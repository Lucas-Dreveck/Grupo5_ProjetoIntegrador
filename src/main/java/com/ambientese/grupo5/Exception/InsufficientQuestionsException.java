package com.ambientese.grupo5.Exception;

public class InsufficientQuestionsException extends RuntimeException {
    public InsufficientQuestionsException(String message) {
        super(message);
    }

    public InsufficientQuestionsException(String message, Throwable cause) {
        super(message, cause);
    }
}