package com.shuyan.library.util;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.shuyan.library.model.Book;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class CsvUtil {
    private static final String FILE_PATH ="src/main/resources/books.csv";
    private static CsvMapper csvmapper = new CsvMapper();
    private static CsvSchema schema = csvmapper.schemaFor(Book.class).withHeader();

    public static List<Book> loadBooks(){
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

    public static void writeBooksToCsv(List<Book> books) {
        try {
            csvmapper.writer(schema).writeValue(new File(FILE_PATH), books);
        } catch (IOException e) {
            throw new RuntimeException("Error writing CSV file", e);
        }
    }



}
