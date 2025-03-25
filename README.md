# library

A simple command-line library management system built with Spring Framework that demonstrates basic CRUD operations.

## Features

- Add new books with title, author, and description
- List all books in the library
- View book details by ID
- Update existing book information
- Delete books from the library

## Technology Stack

- Java 17
- Spring Framework
- Gradle
- Jackson CSV

## Project Structure

```plaintext
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── shuyan/
│   │           └── library/
│   │               ├── LibraryApplication.java
│   │               ├── model/
│   │               │   └── Book.java
│   │               ├── repository/
│   │               │   └── BookRepository.java
│   │               ├── service/
│   │               │   └── BookService.java
│   │               ├── ui/
│   │               │   └── Console.java
│   │               └── exception/
│   │                   ├── LibraryBaseException.java
│   │                   ├── BookNotFoundException.java
│   │                   ├── DuplicateBookException.java
│   │                   └── InvalidBookDataException.java
│   └── resources/
│       └── books.csv