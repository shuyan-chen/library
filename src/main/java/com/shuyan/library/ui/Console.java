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
        while (true) {
            System.out.println(messageSource.getMessage("menu.header", null, locale));
            System.out.println(messageSource.getMessage("menu.display", null, locale));
            System.out.println(messageSource.getMessage("menu.findBookByTitle", null, locale));
            System.out.println(messageSource.getMessage("menu.create", null, locale));
            System.out.println(messageSource.getMessage("menu.edit", null, locale));
            System.out.println(messageSource.getMessage("menu.delete", null, locale));
            System.out.println(messageSource.getMessage("menu.exit", null, locale));

            int choice = readIntInput(
                    messageSource.getMessage("prompt.option", null, locale) + " "
            );
            switch (choice) {
                case 1 -> displayBooks();
                case 2 -> findBookByTitle();
                case 3 -> addBook();
                case 4 -> editBook();
                case 5 -> deleteBook();
                case 0 -> {
                    System.out.println(
                            messageSource.getMessage("message.goodbye", null, locale)
                    );
                    return;
                }
                default -> System.out.println(
                        messageSource.getMessage("error.invalidOption", null, locale)
                );
            }
        }
    }

    private void selectLanguage() {
        String langPrompt = messageSource.getMessage(
                "prompt.enterLanguage", null, Locale.ENGLISH
        );
        System.out.print(langPrompt + " ");

        String input = scanner.nextLine().trim();
        if ("2".equals(input)) {
            locale = new Locale("pl", "PL");
        } else if (!"1".equals(input)) {
            System.out.println(
                    messageSource.getMessage("prompt.invalidLanguage", null, Locale.ENGLISH)
            );
            locale = Locale.ENGLISH;
        } else {
            locale = Locale.ENGLISH;
        }
    }

    private int readIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println(
                        messageSource.getMessage("error.invalidOption", null, locale)
                );
                scanner.nextLine();
            }
        }
    }

    private void displayBooks() {
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println(
                    messageSource.getMessage("message.noBooks", null, locale)
            );
            return;
        }
        System.out.println(messageSource.getMessage("header.bookList", null, locale));
        books.forEach(book -> System.out.println(
                String.format(
                        messageSource.getMessage("format.bookDetails", null, locale),
                        book.getId(), book.getTitle(), book.getAuthor(), book.getDescription()
                )
        ));
    }

    private void findBookByTitle() {
        System.out.print(messageSource.getMessage("prompt.enterBookTitle", null, locale));
        String title = scanner.nextLine().trim();
        Optional<Book> opt = bookService.findByTitle(title);
        if (opt.isPresent()) {
            Book b = opt.get();
            System.out.println(messageSource.getMessage("header.bookFound", null, locale));
            System.out.println(
                    String.format(
                            messageSource.getMessage("format.bookDetails", null, locale),
                            b.getId(), b.getTitle(), b.getAuthor(), b.getDescription()
                    )
            );
        } else {
            System.out.println(
                    messageSource.getMessage("error.bookNotFound", new Object[]{title}, locale)
            );
        }
    }

    private void addBook() {
        System.out.println(messageSource.getMessage("header.addBook", null, locale));
        System.out.print(messageSource.getMessage("prompt.enterBookTitle", null, locale));
        String title = scanner.nextLine().trim();
        System.out.print(messageSource.getMessage("prompt.enterBookAuthor", null, locale));
        String author = scanner.nextLine().trim();
        System.out.print(messageSource.getMessage("prompt.enterBookDescription", null, locale));
        String desc = scanner.nextLine().trim();

        try {
            bookService.addBook(title, author, desc);
            System.out.println(messageSource.getMessage("message.bookCreated", null, locale));
        } catch (IllegalArgumentException ex) {
            System.out.println(
                    messageSource.getMessage("error.invalidOption", null, locale) + ": " + ex.getMessage()
            );
        }
    }

    private void editBook() {
        System.out.println(messageSource.getMessage("header.editBook", null, locale));
        int id = readIntInput(messageSource.getMessage("prompt.enterBookId", null, locale) + " ");
        Book existing = bookService.findBookById(id);
        if (existing == null) {
            System.out.println(
                    messageSource.getMessage("error.bookNotFound", new Object[]{id}, locale)
            );
            return;
        }
        System.out.println(
                String.format(
                        messageSource.getMessage("format.bookDetails", null, locale),
                        existing.getId(), existing.getTitle(),
                        existing.getAuthor(), existing.getDescription()
                )
        );
        System.out.println(messageSource.getMessage("prompt.updateFields", null, locale));

        System.out.print(
                String.format(
                        messageSource.getMessage("prompt.currentValue", null, locale),
                        messageSource.getMessage("prompt.enterBookTitle", null, locale),
                        existing.getTitle()
                )
        );
        String title = scanner.nextLine().trim();
        if (!title.isEmpty()) existing.setTitle(title);

        System.out.print(
                String.format(
                        messageSource.getMessage("prompt.currentValue", null, locale),
                        messageSource.getMessage("prompt.enterBookAuthor", null, locale),
                        existing.getAuthor()
                )
        );
        String author = scanner.nextLine().trim();
        if (!author.isEmpty()) existing.setAuthor(author);

        System.out.print(
                String.format(
                        messageSource.getMessage("prompt.currentValue", null, locale),
                        messageSource.getMessage("prompt.enterBookDescription", null, locale),
                        existing.getDescription()
                )
        );
        String desc = scanner.nextLine().trim();
        if (!desc.isEmpty()) existing.setDescription(desc);

        if (bookService.updateBook(id, existing)) {
            System.out.println(messageSource.getMessage("message.bookUpdated", null, locale));
        } else {
            System.out.println(messageSource.getMessage("error.updateFailed", null, locale));
        }
    }

    private void deleteBook() {
        System.out.println(messageSource.getMessage("header.deleteBook", null, locale));
        int id = readIntInput(messageSource.getMessage("prompt.enterBookId", null, locale) + " ");
        if (bookService.deleteBook(id)) {
            System.out.println(messageSource.getMessage("message.bookDeleted", null, locale));
        } else {
            System.out.println(
                    messageSource.getMessage("error.bookNotFound", new Object[]{id}, locale)
            );
        }
    }


}
