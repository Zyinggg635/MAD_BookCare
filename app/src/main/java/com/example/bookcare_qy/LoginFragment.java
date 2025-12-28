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

import com.example.bookcare_qy.R;
import com.example.bookcare_qy.databinding.FragmentLoginBinding;

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

        // Navigation for Register, and Forgot Password
        binding.textViewRegister.setOnClickListener(v -> navController.navigate(R.id.action_loginFragment_to_registerFragment));
        binding.textViewForgotPassword.setOnClickListener(v -> navController.navigate(R.id.action_loginFragment_to_forgotPasswordFragment));

        // Navigation for the main Login button
        binding.buttonLogin.setOnClickListener(v -> {
            loginUser(navController);
        });
    }

    private void loginUser(NavController navController) {
        String email = binding.editTextEmail.getText().toString();
        String password = binding.editTextPassword.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Email and password cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            navController.navigate(R.id.action_loginFragment_to_navigation_home);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
