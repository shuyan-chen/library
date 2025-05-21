CREATE TABLE authors (
                         id  SERIAL PRIMARY KEY,
                         name VARCHAR(255) UNIQUE NOT NULL,
                         birth_year INT
);

CREATE TABLE genres (
                        id  SERIAL PRIMARY KEY,
                        name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE books (
                       id          SERIAL PRIMARY KEY,
                       title       VARCHAR(255) NOT NULL,
                       author      VARCHAR(255),
                       description TEXT
);


CREATE TABLE book_authors (
                              book_id  INT NOT NULL REFERENCES books(id)  ON DELETE CASCADE,
                              author_id INT NOT NULL REFERENCES authors(id) ON DELETE CASCADE,
                              PRIMARY KEY (book_id, author_id)
);

CREATE TABLE book_genres (
                             book_id  INT NOT NULL REFERENCES books(id)  ON DELETE CASCADE,
                             genre_id INT NOT NULL REFERENCES genres(id) ON DELETE CASCADE,
                             PRIMARY KEY (book_id, genre_id)
);

INSERT INTO authors(name, birth_year) VALUES ('J.K.Rowling', 1965);
INSERT INTO genres(name) VALUES ('Fantasy'), ('Mystery');
INSERT INTO books(title, isbn, published_year)
VALUES ('HarryPotterandthePhilosopher''Stone','9780747532699',1997);

INSERT INTO book_authors
SELECT b.id, a.id FROM books b, authors a
WHERE b.title LIKE 'Harry%' AND a.name='J.K.Rowling';

INSERT INTO book_genres
SELECT b.id, g.id FROM books b, genres g
WHERE b.title LIKE 'Harry%' AND g.name='Fantasy';
