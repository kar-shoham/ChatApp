package com.shoham.chat_app.exception;

public class UserNotInGroupException extends RuntimeException {
    public UserNotInGroupException(String message) {
        super(message);
    }
}
