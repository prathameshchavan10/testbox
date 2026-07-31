package com.testbox.exception;

public class ExamAttemptAlreadyExistsException extends RuntimeException{
    public ExamAttemptAlreadyExistsException(String message) {
        super(message);
}
}