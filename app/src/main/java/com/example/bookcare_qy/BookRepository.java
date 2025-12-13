package com.example.bookcare_qy; // Make sure this matches your package name

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import androidx.annotation.NonNull;

public class BookRepository {

    private DatabaseReference booksRef;

    public BookRepository() {
        // Get a reference to your Realtime Database instance
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://bookcare-82eb6-default-rtdb.asia-southeast1.firebasedatabase.app");
        // Get a reference to the "books" node where all books will be stored
        booksRef = database.getReference("books");
    }

    /**
     * Adds a new book to the Firebase Realtime Database.
     * A unique ID is generated for the new book.
     *
     * @param book The Book object to add. The 'id' field of this object
     *             will be updated with the generated Firebase key.
     */
    public void addBook(Book book) {
        // Generate a unique key for the new book entry
        String newBookId = booksRef.push().getKey();

        if (newBookId != null) {
            // Set the generated ID to the book object
            book.setId(newBookId);

            // Write the book object to the database under the new unique ID
            booksRef.child(newBookId).setValue(book)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            System.out.println("Book added successfully with ID: " + book.getId());
                            // TODO: Add logic here for successful addition (e.g., show a toast, navigate back)
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.err.println("Error adding book: " + e.getMessage());
                            // TODO: Add logic here for failed addition (e.g., show an error message)
                        }
                    });
        } else {
            System.err.println("Failed to generate a new book ID.");
            // TODO: Handle scenario where key generation fails
        }
    }

    // Example of how you might call addBook from an Activity or Fragment
    // For instance, when a user clicks a "Save Book" button:
    public void saveNewBookExample() {
        // Create a new Book object from user input
        Book newBook = new Book(
            null, // ID will be set by Firebase
            "The Mystery of the Old House",
            "B. Author",
            "Used - Good",
            "Donate"
        );
        addBook(newBook);
    }
}
