package com.shuyan.library.exception;

public class BookNotFoundException extends BaseException{
    public BookNotFoundException(Integer id) {
        super("Book not found with id: " + id);
    }
}
