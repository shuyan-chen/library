package com.shuyan.library.repository;

import com.shuyan.library.model.Book;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import static com.shuyan.library.util.CsvUtil.loadBooks;
import static com.shuyan.library.util.CsvUtil.writeBooksToCsv;
import static com.shuyan.library.util.IdGenerator.generateNextId;


@Repository
public class BookRepository {

    public Optional<Book> findById(int id){
        return loadBooks().stream()
                .filter(book -> book.getId() == id)
                .findFirst();
    }

    public Optional<Book> findByTitle(String title){
        return loadBooks().stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .findFirst();
    }

    public List<Book> findAll() {
        return loadBooks();
    }

    public void save(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }

        List<Book> books = loadBooks();

        Optional<Book> existingBook = findById(book.getId());

        if (existingBook.isPresent()) {
            for (int i = 0; i < books.size(); i++) {
                if (books.get(i).getId() == book.getId()) {
                    books.set(i, book);
                    break;
                }
            }
        } else {
            book.setId(generateNextId(books));
            books.add(book);
        }

        writeBooksToCsv(books);
    }


    public void deleteById(int id){
        List<Book> books = findAll();
        books.removeIf(book -> book.getId() == id);
        writeBooksToCsv(books);
    }



}
