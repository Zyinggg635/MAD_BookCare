package com.example.bookcare_qy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.bookcare_qy.databinding.FragmentRegisterBinding;

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

        // Your main "Register" button logic would go here. For example:
        // binding.buttonRegister.setOnClickListener(v -> { /* ... register user ... */ });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
