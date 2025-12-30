package com.example.bookcare_qy;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class activity_book_exchange extends AppCompatActivity {

    private TextView textViewBookCount;
    private TextView textView16; // "X books"
    private TextView textView5; // Points per book
    private TextView textView4; // Total points
    private TextView textView17; // "* X points ="

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_exchange);

        // Initialize Firebase
        FirebaseManager.initializeFirebase();

        // Get book count from Intent
        int bookCount = getIntent().getIntExtra(Constants.EXTRA_BOOK_COUNT, 0);
        String actionType = getIntent().getStringExtra(Constants.EXTRA_ACTION_TYPE);
        
        // Determine points per book based on action type
        int pointsPerBook;
        if ("donation".equalsIgnoreCase(actionType)) {
            pointsPerBook = Constants.POINTS_PER_BOOK_DONATION;
        } else {
            pointsPerBook = Constants.POINTS_PER_BOOK_EXCHANGE;
        }

        // Calculate total points
        int totalPoints = bookCount * pointsPerBook;

        // Find views
        ImageView backButton = findViewById(R.id.backButton);
        textViewBookCount = findViewById(R.id.textView); // Book count for exchange/donation
        textView16 = findViewById(R.id.textView16); // "X books"
        textView5 = findViewById(R.id.textView5); // Points per book value
        textView4 = findViewById(R.id.textView4); // Total points
        textView17 = findViewById(R.id.textView17); // "* X points ="

        // Update UI with calculated values
        if (textViewBookCount != null) {
            textViewBookCount.setText(String.valueOf(bookCount));
        }
        
        if (textView16 != null) {
            textView16.setText(bookCount + " books");
        }
        
        if (textView5 != null) {
            textView5.setText(String.valueOf(pointsPerBook));
        }
        
        if (textView4 != null) {
            textView4.setText(String.valueOf(totalPoints));
        }
        
        if (textView17 != null) {
            textView17.setText("* " + pointsPerBook + " points = ");
        }

        // Back button
        if (backButton != null) {
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
}
