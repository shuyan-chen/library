package com.shuyan.library.ui;

import com.shuyan.library.model.Book;
import com.shuyan.library.service.BookService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class Console {
    private final BookService bookService;
    private final Scanner scanner;
    private final MessageSource messageSource;
    private Locale locale;

    public Console(BookService bookService, MessageSource messageSource) {
        this.bookService = bookService;
        this.scanner = new Scanner(System.in);
        this.messageSource = messageSource;
    }

    public void run() {
        selectLanguage();
        while (true){
            displayMenu();
            int choice = readIntInput("Please select one option: ");
            switch (choice) {
                case 1 -> displayBooks();
                case 2 -> findBookByTitle();
                case 3 -> addBook();
                case 4 -> editBook();
                case 5 -> deleteBook();
                case 6 -> {
                    System.out.println("Goodbye!");
                    return;
                }
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n=== Library Management System ===");
        System.out.println("1. List all books");
        System.out.println("2. Find book by title");
        System.out.println("3. Add new book");
        System.out.println("4. Update book");
        System.out.println("5. Delete book");
        System.out.println("6. Exit");
        System.out.print("\nChoose an option (1-6): ");
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

    private void findBookByTitle() {
            System.out.print("Enter book title to search: ");
            String title = scanner.nextLine().trim();

            Optional<Book> books = bookService.findByTitle(title);

            if (books.isPresent()) {
                Book book = books.get();
                System.out.println("\nBook found:");
                System.out.println("ID: " + book.getId());
                System.out.println("Title: " + book.getTitle());
                System.out.println("Author: " + book.getAuthor());
                System.out.println("Description: " + book.getDescription());
            } else {
                System.out.println("No book found with title: " + title);
            }
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

    private void selectLanguage() {
        System.out.println("=================================");
        System.out.println("Select language: 1. English, 2. Polski");
        String langChoice = scanner.nextLine();
        if ("1".equals(langChoice)) {
            locale = Locale.ENGLISH;
        } else if ("2".equals(langChoice)) {
            locale = new Locale("pl","PL");
        } else {
            System.out.println("Invalid selection, defaulting to English.");
            locale = Locale.ENGLISH;
        }
        System.out.println("DEBUG: Selected locale is " + locale);
        System.out.println("DEBUG: menu.header: " + getMessage("menu.header"));

    }

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, locale);
    }



}
