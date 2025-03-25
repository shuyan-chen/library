package com.shuyan.library.repository;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.shuyan.library.model.Book;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class BookRepository {
    private static final String FILE_PATH ="src/main/resources/books.csv";
    private final CsvMapper csvmapper = new CsvMapper();
    private final CsvSchema schema = csvmapper.schemaFor(Book.class).withHeader();

    private List<Book> loadBooks(){
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                File directory = file.getParentFile();
                if (directory != null && !directory.exists()) {
                    directory.mkdirs();
                }
                file.createNewFile();
                System.out.println("Created new CSV file: " + FILE_PATH);
                return new ArrayList<>();
            } catch (IOException e) {
                throw new RuntimeException("Failed to create CSV file: " + e.getMessage(), e);
            }
        }

        try(Reader reader = Files.newBufferedReader(Paths.get(FILE_PATH)))  {
            MappingIterator<Book> iterator = csvmapper.readerFor(Book.class).with(schema).readValues(reader);
            return iterator.readAll();
        }catch (IOException e){
            System.err.println("Error reading CSV file: " + e.getMessage());
            throw new RuntimeException("Error reading CSV file", e);
        }
    }

    public Optional<Book> findById(int id){
        return loadBooks().stream()
                .filter(book -> book.getId() == id)
                .findFirst();
    }

    public List<Book> findByTitle(String title){
        return loadBooks().stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .collect(Collectors.toList());
    }

    public List<Book> findAll() {
        return loadBooks();
    }

    public void save(Book book){
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        List<Book> books = loadBooks();
        boolean exists = false;
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId() == book.getId()) {
                books.set(i, book);
                exists = true;
                break;
            }
        }
        if (!exists) {
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

    private void writeBooksToCsv(List<Book> books) {
        try {
            csvmapper.writer(schema).writeValue(new File(FILE_PATH), books);
        } catch (IOException e) {
            throw new RuntimeException("Error writing CSV file", e);
        }
    }

    private int generateNextId(List<Book> books) {
        return books.stream()
                .mapToInt(Book::getId)
                .max()
                .orElse(0) + 1;
    }


}
