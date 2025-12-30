package com.example.bookcare_qy;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BookRepository {

    private final DatabaseReference booksRef;
    private final DatabaseReference transactionsRef;

    public BookRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL);
        booksRef = database.getReference(Constants.PATH_BOOKS);
        transactionsRef = database.getReference(Constants.PATH_POINT_TRANSACTIONS);
    }

    public void addBook(Book book) {
        String newBookId = booksRef.push().getKey();
        if (newBookId != null) {
            book.setId(newBookId);
            booksRef.child(newBookId).setValue(book)
                    .addOnSuccessListener(aVoid -> System.out.println("Book added successfully with ID: " + book.getId()))
                    .addOnFailureListener(e -> System.err.println("Error adding book: " + e.getMessage()));
        }
    }

    public void deleteBook(String bookId) {
        if (bookId != null) {
            booksRef.child(bookId).removeValue()
                    .addOnSuccessListener(aVoid -> System.out.println("Book with ID: " + bookId + " deleted successfully."))
                    .addOnFailureListener(e -> System.err.println("Error deleting book: " + e.getMessage()));
        }
    }
    
    public void addPointTransaction(PointTransaction transaction) {
        transactionsRef.push().setValue(transaction)
                .addOnSuccessListener(aVoid -> System.out.println("Point transaction added successfully."))
                .addOnFailureListener(e -> System.err.println("Error adding point transaction: " + e.getMessage()));
    }

    // Example of how you might call addBook from an Activity or Fragment
    public void saveNewBookExample() {
        Book newBook = new Book(
            null, 
            "The Mystery of the Old House",
            "B. Author",
            "Used - Good",
            "Donate",
            "Mystery",
            "exampleOwnerId"
        );
        addBook(newBook);
    }
}
