package com.shuyan.library.util;

import com.shuyan.library.model.Book;

import java.util.List;

public final class IdGenerator {
    public static int generateNextId(List<Book> books) {
        return books.stream()
                .mapToInt(Book::getId)
                .max()
                .orElse(0) + 1;
    }

}
