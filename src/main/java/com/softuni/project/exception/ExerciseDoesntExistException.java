package com.softuni.project.exception;

public class ExerciseDoesntExistException extends RuntimeException {
    public ExerciseDoesntExistException(String message) {
        super(message);
    }
}
