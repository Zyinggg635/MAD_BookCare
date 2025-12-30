package com.example.bookcare_qy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.bookcare_qy.databinding.ActivityMainBinding;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

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
        
        // Initialize Firebase early (before setContentView)
        // Firebase auto-initializes from google-services.json, but we set up database persistence here
        FirebaseManager.initializeFirebase();

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(binding.navView, navController);

            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                int destinationId = destination.getId();

                if (destinationId == R.id.welcomeFragment ||
                        destinationId == R.id.loginFragment ||
                        destinationId == R.id.registerFragment ||
                        destinationId == R.id.forgotPasswordFragment) {
                    binding.navView.setVisibility(View.GONE);
                } else {
                    binding.navView.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    // onSupportNavigateUp is no longer needed as we've removed the ActionBar.
}
