package com.maxiflexy.dreamdevs.librarymanagementsystem.util;

import com.maxiflexy.dreamdevs.librarymanagementsystem.model.Book;
import com.maxiflexy.dreamdevs.librarymanagementsystem.model.Member;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVExporter {
    private static final Logger logger = new Logger();
    private static final String BOOKS_CSV_PATH = "src/main/resources/books.csv";
    private static final String MEMBERS_CSV_PATH = "src/main/resources/members.csv";

    public void exportBooksToCSV(List<Book> books) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_CSV_PATH))) {
            // Write header
            writer.write("book_id,title,author,genre,available_copies");
            writer.newLine();

            // Write data
            for (Book book : books) {
                String line = String.join(",",
                        String.valueOf(book.getBookId()),
                        escapeCSV(book.getTitle()),
                        escapeCSV(book.getAuthor()),
                        escapeCSV(book.getGenre()),
                        String.valueOf(book.getAvailableCopies())
                );
                writer.write(line);
                writer.newLine();
            }

            logger.log("Books exported to CSV file: " + BOOKS_CSV_PATH);
        } catch (IOException e) {
            logger.log("Error exporting books to CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void exportMembersToCSV(List<Member> members) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MEMBERS_CSV_PATH))) {
            // Write header
            writer.write("member_id,name,email,phone");
            writer.newLine();

            // Write data
            for (Member member : members) {
                String line = String.join(",",
                        String.valueOf(member.getMemberId()),
                        escapeCSV(member.getName()),
                        escapeCSV(member.getEmail()),
                        escapeCSV(member.getPhone())
                );
                writer.write(line);
                writer.newLine();
            }

            logger.log("Members exported to CSV file: " + MEMBERS_CSV_PATH);
        } catch (IOException e) {
            logger.log("Error exporting members to CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String escapeCSV(String value) {
        if (value == null) {
            return "";
        }

        // If the value contains commas, quotes, or newlines, wrap it in quotes and escape any quotes
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }

        return value;
    }
}
