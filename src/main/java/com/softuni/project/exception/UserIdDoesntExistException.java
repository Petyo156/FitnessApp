package com.softuni.project.exception;

public class UserIdDoesntExistException extends RuntimeException {
    public UserIdDoesntExistException(String message) {
        super(message);
    }
}
