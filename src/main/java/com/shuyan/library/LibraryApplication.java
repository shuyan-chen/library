package com.shuyan.library;

import com.shuyan.library.config.AppConfig;
import com.shuyan.library.ui.Console;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


public class LibraryApplication {


    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        Console console = context.getBean(Console.class);
        console.run();
        context.close();
    }

}
