package com.wxbc.exception;

public class DuplicateMailAddressException extends Exception{
    public DuplicateMailAddressException(String message) {
        super(message);
    }
}
