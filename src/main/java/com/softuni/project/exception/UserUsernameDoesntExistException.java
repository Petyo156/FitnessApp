package com.softuni.project.exception;

public class UserUsernameDoesntExistException extends RuntimeException {
    public UserUsernameDoesntExistException(String message) {
        super(message);
    }
}
