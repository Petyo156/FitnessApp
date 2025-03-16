package com.softuni.project.exception;

public class LogDoesntExistException extends RuntimeException{
    public LogDoesntExistException(String message) {
        super(message);
    }
}
