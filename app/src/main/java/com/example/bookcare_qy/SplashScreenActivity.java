package com.example.bookcare_qy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Initialize Firebase
        FirebaseManager.initializeFirebase();

        // Check if user is logged in after a delay
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                checkAuthenticationAndNavigate();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void checkAuthenticationAndNavigate() {
        FirebaseAuth auth = FirebaseManager.getAuth();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            // User is logged in, navigate to MainActivity (which contains the home fragment)
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            // User is not logged in, navigate to MainActivity (which shows welcome/login)
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
        }
        
        finish(); // Close splash screen
    }
}


