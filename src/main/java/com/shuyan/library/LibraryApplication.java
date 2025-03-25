package com.shuyan.library;

import com.shuyan.library.ui.Console;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.shuyan.library")
public class LibraryApplication {
    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(LibraryApplication.class)) {
            System.out.println("=== Library Management System ===");
            var ui = context.getBean(Console.class);
            ui.run();
        } catch (Exception e) {
            System.err.println("Application failed to start: " + e.getMessage());
            System.exit(1);
        }
    }
}
