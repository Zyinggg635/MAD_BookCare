package com.example.bookcare_qy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * BookDonationActivity - Allows users to register books for donation
 * 
 * NOTE: The current layout (activity_book_donation.xml) shows a calculation screen.
 * According to the requirements, this activity should have:
 * - Form fields: Book Title, Author, Genre (spinner), Condition (spinner)
 * - "Add Book" button
 * - RecyclerView showing books added in current session
 * - "Confirm Donation" button
 * 
 * You may need to update activity_book_donation.xml to include these form elements
 * or create a new layout file with the form fields.
 */
public class activity_book_donation extends AppCompatActivity {

    private EditText editTextBookTitle;
    private EditText editTextAuthor;
    private Spinner spinnerGenre;
    private Spinner spinnerCondition;
    private Button buttonAddBook;
    private Button buttonConfirmDonation;
    private RecyclerView recyclerViewBooks;
    
    private List<Book> booksList;
    private BookAdapter bookAdapter;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_donation);

        // Initialize Firebase
        FirebaseManager.initializeFirebase();
        currentUser = FirebaseManager.getAuth().getCurrentUser();
        
        if (currentUser == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize views
        // NOTE: These views may not exist in the current layout - you may need to update activity_book_donation.xml
        ImageView backButton = findViewById(R.id.backButton);
        editTextBookTitle = findViewById(R.id.editTextBookTitle);
        editTextAuthor = findViewById(R.id.editTextAuthor);
        spinnerGenre = findViewById(R.id.spinnerGenre);
        spinnerCondition = findViewById(R.id.spinnerCondition);
        buttonAddBook = findViewById(R.id.buttonAddBook);
        buttonConfirmDonation = findViewById(R.id.buttonConfirmDonation);
        recyclerViewBooks = findViewById(R.id.recyclerViewBooks);
        
        // Check if form views exist - if not, the layout needs to be updated
        if (editTextBookTitle == null || editTextAuthor == null || spinnerGenre == null || 
            spinnerCondition == null || buttonAddBook == null || buttonConfirmDonation == null || 
            recyclerViewBooks == null) {
            Toast.makeText(this, "Layout needs to be updated with form fields. Please update activity_book_donation.xml", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Initialize books list
        booksList = new ArrayList<>();
        
        // Setup RecyclerView
        recyclerViewBooks.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter(booksList, 
            book -> {
                // Handle item click if needed
            },
            bookId -> {
                // Handle delete
                removeBookFromList(bookId);
            });
        recyclerViewBooks.setAdapter(bookAdapter);

        // Back button
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        // Add Book button
        buttonAddBook.setOnClickListener(v -> addBookToList());

        // Confirm Donation button
        buttonConfirmDonation.setOnClickListener(v -> confirmDonation());
    }

    private void addBookToList() {
        String title = editTextBookTitle.getText().toString().trim();
        String author = editTextAuthor.getText().toString().trim();
        String genre = spinnerGenre.getSelectedItem() != null ? 
                       spinnerGenre.getSelectedItem().toString() : "";
        String condition = spinnerCondition.getSelectedItem() != null ? 
                          spinnerCondition.getSelectedItem().toString() : "";

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "Please enter book title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(author)) {
            Toast.makeText(this, "Please enter author name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create book object with temporary ID for list display
        Book book = new Book("temp_" + System.currentTimeMillis() + "_" + booksList.size(), 
                            title, author, condition, "Donation", genre, currentUser.getUid());
        booksList.add(book);
        bookAdapter.notifyDataSetChanged();

        // Clear form
        editTextBookTitle.setText("");
        editTextAuthor.setText("");
        spinnerGenre.setSelection(0);
        spinnerCondition.setSelection(0);

        Toast.makeText(this, "Book added to donation list", Toast.LENGTH_SHORT).show();
    }

    private void removeBookFromList(String bookId) {
        // Since books in list don't have IDs yet, remove by index
        // This is a simple implementation - you might want to improve it
        for (int i = booksList.size() - 1; i >= 0; i--) {
            if (booksList.get(i).getId() == null || booksList.get(i).getId().equals(bookId)) {
                booksList.remove(i);
                bookAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    private void confirmDonation() {
        if (booksList.isEmpty()) {
            Toast.makeText(this, "Please add at least one book to donate", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save books to Firebase
        DatabaseReference booksRef = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL)
                .getReference(Constants.PATH_BOOKS);
        
        DatabaseReference userRef = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL)
                .getReference(Constants.PATH_USERS)
                .child(currentUser.getUid());

        int totalPoints = booksList.size() * Constants.POINTS_PER_BOOK_DONATION;

        // Save each book
        for (Book book : booksList) {
            String bookId = booksRef.push().getKey();
            if (bookId != null) {
                book.setId(bookId);
                booksRef.child(bookId).setValue(book);
            }
        }

        // Update user's total points and books donated count
        userRef.child("totalPoints").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Integer currentPoints = task.getResult().getValue(Integer.class);
                if (currentPoints == null) currentPoints = 0;
                
                userRef.child("totalPoints").setValue(currentPoints + totalPoints);
                userRef.child("booksDonated").get().addOnCompleteListener(task2 -> {
                    Integer currentDonated = task2.getResult().getValue(Integer.class);
                    if (currentDonated == null) currentDonated = 0;
                    userRef.child("booksDonated").setValue(currentDonated + booksList.size());
                });
            }
        });

        // Add to activity feed
        userRef.child("username").get().addOnCompleteListener(task -> {
            String username = task.getResult().getValue(String.class);
            if (username == null) username = currentUser.getEmail();
            
            ActivityFeedItem feedItem = new ActivityFeedItem(
                currentUser.getUid(),
                username,
                Constants.ACTION_DONATED,
                booksList.size(),
                totalPoints
            );
            
            DatabaseReference feedRef = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL)
                    .getReference(Constants.PATH_ACTIVITY_FEED);
            feedRef.push().setValue(feedItem);
        });

        // Navigate to BookExchangeActivity to show calculation
        Intent intent = new Intent(this, activity_book_exchange.class);
        intent.putExtra(Constants.EXTRA_BOOK_COUNT, booksList.size());
        intent.putExtra(Constants.EXTRA_ACTION_TYPE, "donation");
        startActivity(intent);
        
        finish();
    }
}
