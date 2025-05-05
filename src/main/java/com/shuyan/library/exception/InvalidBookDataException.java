package com.shuyan.library.exception;

public class InvalidBookDataException extends BaseException{
    public InvalidBookDataException(String message) {
        super("Invalid book data: " + message);
    }

}
