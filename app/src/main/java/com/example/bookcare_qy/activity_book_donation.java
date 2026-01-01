package com.example.bookcare_qy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.example.bookcare_qy.databinding.ActivityBookDonationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Activity for displaying Book Donation Calculation Details.
 * Uses activity_book_donation.xml layout.
 */
public class activity_book_donation extends AppCompatActivity {

    private ActivityBookDonationBinding binding;
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
        
        // Use DataBinding to set the content view to activity_book_donation.xml
        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_donation);

        // Initialize Firebase
        FirebaseManager.initializeFirebase();

        // Donation points are fixed at 10
        int pointsPerBook = Constants.POINTS_PER_BOOK_DONATION;
        binding.setPointsPerBook(pointsPerBook);

        // Check if book count was passed via intent
        if (getIntent().hasExtra(Constants.EXTRA_BOOK_COUNT)) {
            int bookCount = getIntent().getIntExtra(Constants.EXTRA_BOOK_COUNT, 0);
            binding.setBookCount(bookCount);
        } else {
            // If no intent extra, fetch from Firebase (Simulated)
            loadBookCountFromFirebase();
        }

        // Back button logic
        if (binding.backButton != null) {
            binding.backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    private void loadBookCountFromFirebase() {
        // SIMULATED DATABASE RETRIEVAL FOR TESTING
        // Assuming database returns 5 donations
        
        int simulatedBookCount = 5; 
        binding.setBookCount(simulatedBookCount);

        // NOTE: The actual Firebase code is commented out below to force these values for testing.
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
                    binding.setBookCount(user.getBooksDonated());
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
