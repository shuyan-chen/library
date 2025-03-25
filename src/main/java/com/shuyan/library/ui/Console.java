package com.shuyan.library.ui;

import com.shuyan.library.model.Book;
import com.shuyan.library.service.BookService;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Console {
    private final BookService bookService;
    private final Scanner scanner;

    public Console(BookService bookService) {
        this.bookService = bookService;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            displayMenu();
            int choice = readIntInput("Please select one option: ");
            switch (choice) {
                case 1 -> displayBooks();
                case 2 -> addBook();
                case 3 -> editBook();
                case 4 -> deleteBook();
                case 5 -> {
                    System.out.println("Goodbye!");
                    return;
                }
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n=== Library Management System ===");
        System.out.println("1. Display book list");
        System.out.println("2. Add a book");
        System.out.println("3. Edit a book");
        System.out.println("4. Delete a book");
        System.out.println("5. Exit");
        System.out.println("============================");
    }

    private int readIntInput(String s) {
        while (true) {
            System.out.print(s);
            try {
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number.");
                scanner.nextLine();
            }
        }
    }

    private void displayBooks(){
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }
        System.out.println("\n=== Book List ===");
        books.forEach(book -> System.out.println(
                String.format("ID: %d\nTitle: %s\nAuthor: %s\nDescription: %s\n",
                        book.getId(), book.getTitle(), book.getAuthor(), book.getDescription())
        ));
    }

    private void addBook(){
        System.out.println("\n=== Add New Book ===");
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Author: ");
        String author = scanner.nextLine().trim();
        System.out.print("Description: ");
        String description = scanner.nextLine().trim();

        try {
            bookService.addBook(title, author, description);
            System.out.println("Book added successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void editBook() {
        System.out.println("\n=== Edit Book ===");
        int id = readIntInput("Enter the book ID to edit: ");

        Optional<Book> existingBook = Optional.ofNullable(bookService.findBookById(id));
        if (existingBook.isEmpty()) {
            System.out.println("Book not found with ID: " + id);
            return;
        }

        Book book = existingBook.get();
        System.out.println("Current book details:");
        System.out.println(book);
        System.out.println("\nEnter new details (press Enter to keep current value):");

        System.out.printf("New title [%s]: ", book.getTitle());
        String title = scanner.nextLine().trim();

        System.out.printf("New author [%s]: ", book.getAuthor());
        String author = scanner.nextLine().trim();

        System.out.printf("New description [%s]: ", book.getDescription());
        String description = scanner.nextLine().trim();

        if (!title.isEmpty()) book.setTitle(title);
        if (!author.isEmpty()) book.setAuthor(author);
        if (!description.isEmpty()) book.setDescription(description);

        if (bookService.updateBook(id, book)) {
            System.out.println("Book updated successfully!");
        } else {
            System.out.println("Failed to update book.");
        }
    }

    private void deleteBook() {
        System.out.println("\n=== Delete Book ===");
        int id = readIntInput("Enter the book ID to delete: ");

        if (bookService.deleteBook(id)) {
            System.out.println("Book successfully deleted!");
        } else {
            System.out.println("Book not found with ID: " + id);
        }
    }



}
