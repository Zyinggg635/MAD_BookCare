package com.example.bookcare_qy;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.bookcare_qy.databinding.FragmentLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);

        // Corrected navigation for Register
        binding.textViewRegister.setOnClickListener(v -> navController.navigate(R.id.action_loginFragment_to_registerFragment));
        binding.textViewForgotPassword.setOnClickListener(v -> navController.navigate(R.id.action_loginFragment_to_forgotPasswordFragment));

        // Navigation for the main Login button
        binding.buttonLogin.setOnClickListener(v -> {
            loginUser(navController);
        });
    }

    private void loginUser(NavController navController) {
        String email = binding.editTextEmail.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Email and password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading state (you can add a progress bar here)
        binding.buttonLogin.setEnabled(false);

        // Ensure Firebase is initialized
        FirebaseManager.initializeFirebase();
        
        // Get Firebase Auth instance (auto-initializes from google-services.json)
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        binding.buttonLogin.setEnabled(true);
                        
                        if (task.isSuccessful()) {
                            // Sign in success, fetch user data and navigate
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            if (firebaseUser != null) {
                                fetchUserDataAndNavigate(navController, firebaseUser.getUid());
                            } else {
                                navController.navigate(R.id.action_loginFragment_to_navigation_home);
                            }
                        } else {
                            // Sign in failed
                            String errorMessage = "Login failed";
                            if (task.getException() != null) {
                                errorMessage = task.getException().getMessage();
                            }
                            Toast.makeText(getContext(), errorMessage != null ? errorMessage : "Invalid credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void fetchUserDataAndNavigate(NavController navController, String userId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL)
                .getReference(Constants.PATH_USERS)
                .child(userId);
        
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // User data fetched successfully (or doesn't exist yet)
                // Navigate to home
                navController.navigate(R.id.action_loginFragment_to_navigation_home);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Even if fetch fails, navigate to home
                navController.navigate(R.id.action_loginFragment_to_navigation_home);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
