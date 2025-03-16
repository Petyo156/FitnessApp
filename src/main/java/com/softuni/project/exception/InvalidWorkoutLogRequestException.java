package com.softuni.project.exception;

public class InvalidWorkoutLogRequestException extends RuntimeException {
    public InvalidWorkoutLogRequestException(String message) {
        super(message);
    }
}
