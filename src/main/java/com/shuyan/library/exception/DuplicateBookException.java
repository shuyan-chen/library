package com.shuyan.library.exception;

public class DuplicateBookException extends BaseException{
    public DuplicateBookException(String title) {
        super("Book with title already exists: " + title);
    }
}
