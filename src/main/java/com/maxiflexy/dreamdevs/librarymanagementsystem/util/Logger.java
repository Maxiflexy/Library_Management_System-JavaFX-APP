package com.maxiflexy.dreamdevs.librarymanagementsystem.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String LOG_FILE = "src/main/resources/library_log.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void log(String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String logEntry = timestamp + " - " + message;

        System.out.println(logEntry);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(logEntry);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

/*
Things Fall Apart	Chinua Achebe	Historical, Fiction
Half of a Yellow Sun	Chimamanda Ngozi Adichie	Historical, War Drama
Purple Hibiscus	Chimamanda Ngozi Adichie	Fiction, Coming-of-age
The Joys of Motherhood	Buchi Emecheta	Feminism, Historical
The Fishermen	Chigozie Obioma	Fiction, Family Drama
Stay With Me	Ayọ̀bámi Adébáyọ̀	Fiction, Family Drama
My Sister, the Serial Killer	Oyinkan Braithwaite	Thriller, Dark Humor
Welcome to Lagos	Chibundu Onuzo	Fiction, Satire
Sozaboy	Ken Saro-Wiwa	War, Coming-of-age
Aké: The Years of Childhood	Wole Soyinka	Autobiography


Title	Author	Genre
To Kill a Mockingbird	Harper Lee	Fiction, Drama
1984	George Orwell	Dystopian, Political
The Great Gatsby	F. Scott Fitzgerald	Fiction, Classic
Harry Potter Series	J.K. Rowling	Fantasy, Adventure
The Lord of the Rings	J.R.R. Tolkien	Fantasy, Epic
The Catcher in the Rye	J.D. Salinger	Fiction, Coming-of-age
Pride and Prejudice	Jane Austen	Romance, Classic
The Alchemist	Paulo Coelho	Fiction, Inspirational
The Da Vinci Code	Dan Brown	Thriller, Mystery
Atomic Habits	James Clear	Self-Help, Psychology
 */