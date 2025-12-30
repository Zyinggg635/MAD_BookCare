package com.example.bookcare_qy;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.bookcare_qy.databinding.FragmentRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.widget.Spinner;
import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // This is the modern, backward-compatible way to handle window insets.
        ViewCompat.setOnApplyWindowInsetsListener(binding.registerLayout, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Apply the insets as a margin to the view. This moves the view out from behind the bars.
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            mlp.leftMargin = insets.left;
            mlp.bottomMargin = insets.bottom;
            mlp.rightMargin = insets.right;
            v.setLayoutParams(mlp);

            // Adjust top margin of specific views if necessary (e.g., a back button)
            ViewGroup.MarginLayoutParams buttonMlp = (ViewGroup.MarginLayoutParams) binding.buttonBack.getLayoutParams();
            buttonMlp.topMargin = insets.top + (int) (16 * getResources().getDisplayMetrics().density); // Add original margin
            binding.buttonBack.setLayoutParams(buttonMlp);

            // Return CONSUMED to prevent other listeners from overriding the insets
            return WindowInsetsCompat.CONSUMED;
        });

        final NavController navController = Navigation.findNavController(view);

        // Back button navigation
        binding.buttonBack.setOnClickListener(v -> navController.navigateUp());

        // When the user clicks "Login", navigate to the LoginFragment.
        binding.textViewLogin.setOnClickListener(v -> {
            navController.navigate(R.id.action_registerFragment_to_loginFragment);
        });

        // Register button click listener
        binding.buttonRegister.setOnClickListener(v -> {
            registerUser(navController);
        });
    }

    private void registerUser(NavController navController) {
        String username = binding.editTextUsername.getText().toString().trim();
        String email = binding.editTextEmail.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString().trim();
        String phone = binding.editTextPhone.getText().toString().trim();
        String address = binding.editTextAddress.getText().toString().trim();
        
        Spinner genreSpinner = getView().findViewById(R.id.spinnerGenrePreference);
        String genrePreference = genreSpinner.getSelectedItem() != null ? 
                                genreSpinner.getSelectedItem().toString() : "";

        // Validate inputs
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getContext(), "Username cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Email cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(getContext(), "Invalid email format", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading state
        binding.buttonRegister.setEnabled(false);

        // Ensure Firebase is initialized
        FirebaseManager.initializeFirebase();
        
        // Get Firebase Auth instance (auto-initializes from google-services.json)
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        binding.buttonRegister.setEnabled(true);
                        
                        if (task.isSuccessful()) {
                            // Registration successful, create user profile in database
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            if (firebaseUser != null) {
                                createUserProfile(firebaseUser.getUid(), username, email, phone, address, genrePreference, navController);
                            } else {
                                Toast.makeText(getContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Registration failed
                            String errorMessage = "Registration failed";
                            if (task.getException() != null) {
                                errorMessage = task.getException().getMessage();
                            }
                            Toast.makeText(getContext(), errorMessage != null ? errorMessage : "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void createUserProfile(String userId, String username, String email, String phone, String address, String genrePreference, NavController navController) {
        User user = new User(userId, username, email, phone, address, genrePreference);
        
        DatabaseReference userRef = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL)
                .getReference(Constants.PATH_USERS)
                .child(userId);
        
        userRef.setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Registration successful!", Toast.LENGTH_SHORT).show();
                            // Navigate to home (user is already authenticated after registration)
                            // Note: You may need to add an action from registerFragment to navigation_home in nav_graph.xml
                            // For now, navigating to login - user can then login with their new credentials
                            navController.navigate(R.id.action_registerFragment_to_loginFragment);
                        } else {
                            Toast.makeText(getContext(), "Failed to create user profile", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        return pattern.matcher(email).matches();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
