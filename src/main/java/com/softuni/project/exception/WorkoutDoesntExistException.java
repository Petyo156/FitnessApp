package com.softuni.project.exception;

public class WorkoutDoesntExistException extends RuntimeException {
    public WorkoutDoesntExistException(String message) {
        super(message);
    }
}
