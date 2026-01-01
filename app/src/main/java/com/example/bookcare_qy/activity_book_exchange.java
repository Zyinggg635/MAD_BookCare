package com.example.bookcare_qy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.example.bookcare_qy.databinding.ActivityBookExchangeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class activity_book_exchange extends AppCompatActivity {

    private ActivityBookExchangeBinding binding;
    private DatabaseReference userRef;
    private ValueEventListener userEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // --- START: BLUE MODE THEME LOGIC ---
        SharedPreferences sharedPreferences = getSharedPreferences("ThemePrefs", MODE_PRIVATE);
        boolean isBlueMode = sharedPreferences.getBoolean("isBlueMode", false);
        if (isBlueMode) {
            setTheme(R.style.Theme_Bookcare_qy_Blue);
        }
        // --- END: BLUE MODE THEME LOGIC ---

        super.onCreate(savedInstanceState);
        
        // Initialize Binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_exchange);

        // Initialize Firebase
        FirebaseManager.initializeFirebase();

        // Get action type
        String actionType = getIntent().getStringExtra(Constants.EXTRA_ACTION_TYPE);
        boolean isDonation = "donation".equalsIgnoreCase(actionType);
        
        // Determine points per book and UI title
        int pointsPerBook;
        if (isDonation) {
            pointsPerBook = Constants.POINTS_PER_BOOK_DONATION;
            binding.bookExchangesText1.setText(R.string.book_donation_label);
        } else {
            pointsPerBook = Constants.POINTS_PER_BOOK_EXCHANGE;
            binding.bookExchangesText1.setText(R.string.book_exchange_label);
        }
        
        // Set points per book immediately as it is constant
        binding.setPointsPerBook(pointsPerBook);

        // Check if book count was passed via intent (e.g. from donation form)
        if (getIntent().hasExtra(Constants.EXTRA_BOOK_COUNT)) {
            int bookCount = getIntent().getIntExtra(Constants.EXTRA_BOOK_COUNT, 0);
            binding.setBookCount(bookCount);
        } else {
            // If no intent extra, fetch from Firebase
            loadBookCountFromFirebase(isDonation);
        }

        // Back button
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadBookCountFromFirebase(boolean isDonation) {
        // SIMULATED DATABASE RETRIEVAL FOR TESTING
        // Assuming database returns 3 exchanges and 5 donations
        
        int simulatedBookCount;
        if (isDonation) {
            simulatedBookCount = 5; // 5 Donations
        } else {
            simulatedBookCount = 3; // 3 Exchanges
        }
        binding.setBookCount(simulatedBookCount);

        // NOTE: The actual Firebase code is commented out below to force these values for testing.
        // To revert to real database values, uncomment the code below and remove the simulated logic above.
        
        /*
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;
        
        userRef = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL)
                .getReference(Constants.PATH_USERS)
                .child(currentUser.getUid());
        
        userEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (binding == null) return;
                
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    int bookCount;
                    if (isDonation) {
                        bookCount = user.getBooksDonated();
                    } else {
                        bookCount = user.getBooksExchanged();
                    }
                    binding.setBookCount(bookCount);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        };
        userRef.addValueEventListener(userEventListener);
        */
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userRef != null && userEventListener != null) {
            userRef.removeEventListener(userEventListener);
        }
    }
}
