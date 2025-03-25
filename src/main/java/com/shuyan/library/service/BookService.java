package com.shuyan.library.service;

import com.shuyan.library.exception.BookNotFoundException;
import com.shuyan.library.exception.InvalidBookDataException;
import com.shuyan.library.model.Book;
import com.shuyan.library.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository repository){
        this.bookRepository = repository;
    }

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    public Book findBookById(int id) {
        if (id < 0){
            throw new IllegalArgumentException("ID must be positive");
        }
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    public List<Book> findBookByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        return bookRepository.findByTitle(title);
    }

    public void addBook(String title, String author, String description) {
        if (title == null || title.trim().isEmpty() || author == null || author.trim().isEmpty()) {
            throw new InvalidBookDataException("Title and author cannot be empty");
        }
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setDescription(description);
        bookRepository.save(book);
    }

    public boolean updateBook(int id, Book updatedBook) {
        if (bookRepository.findById(id).isPresent()) {
            updatedBook.setId(id);
            bookRepository.save(updatedBook);
            return true;
        }
        return false;
    }


    public boolean deleteBook(int id) {
        if (bookRepository.findById(id).isPresent()) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }


}
