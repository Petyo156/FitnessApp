package com.softuni.project.exception;

public class ProgramDoesntExistException extends RuntimeException {
    public ProgramDoesntExistException(String message) {
        super(message);
    }
}
