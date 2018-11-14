package com.wxbc.exception;

public class DuplicateUserNameException extends Exception{
    public DuplicateUserNameException(String message) {
        super(message);
    }
}
