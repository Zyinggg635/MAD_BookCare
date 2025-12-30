package com.example.bookcare_qy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowMetrics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.bookcare_qy.R;
import com.example.bookcare_qy.databinding.FragmentForgotPasswordBinding;

public class ForgotPasswordFragment extends Fragment {

    private FragmentForgotPasswordBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // This block runs after the view is created and ensures it's ready for measurement.
        view.post(() -> {
            // Get the height of the status bar
            final WindowMetrics metrics = requireActivity().getWindowManager().getCurrentWindowMetrics();
            final android.graphics.Insets insets = metrics.getWindowInsets().getInsets(android.view.WindowInsets.Type.statusBars());
            int statusBarHeight = insets.top;

            // Get the current layout parameters of the back button
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) binding.buttonBack.getLayoutParams();

            // The original top margin is 16dp. We convert it to pixels.
            int originalTopMarginInPixels = (int) (16 * getResources().getDisplayMetrics().density);

            // Add the status bar height to the existing top margin
            layoutParams.topMargin = originalTopMarginInPixels + statusBarHeight;

            // Apply the new layout parameters to the button
            binding.buttonBack.setLayoutParams(layoutParams);
        });

        final NavController navController = Navigation.findNavController(view);

        // Back button navigation (top left arrow)
        binding.buttonBack.setOnClickListener(v -> navController.navigateUp());

        // When the user clicks "Back to Login", navigate to the LoginFragment.
        binding.textViewBackToLogin.setOnClickListener(v -> {
            navController.navigate(R.id.action_forgotPasswordFragment_to_loginFragment);
        });

        binding.buttonSendResetLink.setOnClickListener(v -> {
            sendResetLink();
        });
    }
    
    private void sendResetLink() {
        String email = "";
        if (binding.textInputLayoutEmail.getEditText() != null) {
            email = binding.textInputLayoutEmail.getEditText().getText().toString().trim();
        }
        
        if (email.isEmpty()) {
            android.widget.Toast.makeText(getContext(), "Please enter your email address", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Note: Email template customization must be done in Firebase Console
        // Go to Authentication > Templates > Password reset
        // The template uses %DISPLAY_NAME% and %APP_NAME% placeholders
        com.google.firebase.auth.FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        android.widget.Toast.makeText(getContext(), "Password reset email sent!", android.widget.Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(requireView()).navigateUp();
                    } else {
                        String errorMessage = "Failed to send reset email";
                        if (task.getException() != null) {
                            errorMessage = task.getException().getMessage();
                        }
                        android.widget.Toast.makeText(getContext(), errorMessage, android.widget.Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
