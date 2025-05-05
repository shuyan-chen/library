package com.shuyan.library.exception;

public class BookUpdateException extends BaseException{
    public BookUpdateException(Integer id) {
        super("Failed to update book with id: " + id);
    }
}
