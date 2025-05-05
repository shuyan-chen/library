package com.shuyan.library.exception;

public class BookDeleteException extends BaseException{
    public BookDeleteException(Integer id) {
        super("Failed to delete book with id: " + id );
    }
}
