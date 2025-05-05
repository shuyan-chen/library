package com.shuyan.library;

import com.shuyan.library.config.AppConfig;
import com.shuyan.library.ui.Console;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class LibraryApplication {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext ctx =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {
            ctx.getBean(Console.class).run();
        }
    }

}
