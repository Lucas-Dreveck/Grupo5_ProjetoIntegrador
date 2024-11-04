package com.ambientese.grupo5.Exception;

public class InvalidQuestionCountException extends RuntimeException {
    public InvalidQuestionCountException(String message) {
        super(message);
    }

    public InvalidQuestionCountException(String message, Throwable cause) {
        super(message, cause);
    }
}

