package com.shuyan.library.repository;

import com.shuyan.library.model.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookRepository {

    private final JdbcTemplate jdbc;
    private final RowMapper<Book> mapper = (rs, n) -> new Book(
            rs.getInt("id"),
            rs.getString("title"),
            rs.getString("author"),
            rs.getString("description")
    );

    public BookRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<Book> findById(int id) {
        return jdbc.query("SELECT * FROM books WHERE id = ?", mapper, id)
                .stream().findFirst();
    }

    public Optional<Book> findByTitle(String title) {
        return jdbc.query("SELECT * FROM books WHERE LOWER(title) = LOWER(?)",
                        mapper, title)
                .stream().findFirst();
    }

    public List<Book> findAll() {
        return jdbc.query("SELECT * FROM books ORDER BY id", mapper);
    }

    public void save(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }

        if (book.getId() == 0) {
            Integer id = jdbc.queryForObject(
                    "INSERT INTO books(title, author, description) " +
                            "VALUES (?,?,?) RETURNING id",
                    Integer.class,
                    book.getTitle(), book.getAuthor(), book.getDescription());
            book.setId(id);
        } else {
            jdbc.update(
                    "UPDATE books SET title=?, author=?, description=? WHERE id=?",
                    book.getTitle(), book.getAuthor(),
                    book.getDescription(), book.getId());
        }
    }

    public void deleteById(int id) {
        jdbc.update("DELETE FROM books WHERE id = ?", id);
    }
}

