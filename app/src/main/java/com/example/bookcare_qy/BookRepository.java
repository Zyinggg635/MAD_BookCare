package com.example.bookcare_qy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Simple in-memory store to share books across screens until Firebase is added.
 */
public final class BookRepository {

    private static final ArrayList<Book> books = new ArrayList<>();

    private BookRepository() { }

    public static List<Book> getBooks() {
        return Collections.unmodifiableList(books);
    }

    public static void addBook(Book book) {
        books.add(book);
    }

    public static void seedIfEmpty() {
        if (!books.isEmpty()) {
            return;
        }

        books.add(new Book("The Midnight Library", "Matt Haig", "Exchange", 12, 3, "hannah"));
        books.add(new Book("Atomic Habits", "James Clear", "Exchange", 28, 9, "amir"));
        books.add(new Book("The Two Towers", "J.R.R. Tolkien", "Donate", 8, 2, "samwise"));
        books.add(new Book("The Obstacle Is The Way", "Ryan Holiday", "Exchange", 16, 4, "marcus"));
        books.add(new Book("You Are What You Risk", "Michele Wucker", "Donate", 6, 1, "grace"));
        books.add(new Book("The Psychology of Money", "Morgan Housel", "Exchange", 31, 12, "alex"));
    }
}


