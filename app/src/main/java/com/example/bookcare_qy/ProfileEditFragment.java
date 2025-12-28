package com.example.bookcare_qy;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowMetrics;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.bookcare_qy.SharedViewModel;
import com.example.bookcare_qy.databinding.FragmentProfileEditBinding;

public class ProfileEditFragment extends Fragment {

    private FragmentProfileEditBinding binding;
    private SharedViewModel sharedViewModel;

    private final ActivityResultLauncher<String> getContentLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), (Uri uri) -> {
                if (uri != null) {
                    sharedViewModel.profileImageUri.setValue(uri);
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileEditBinding.inflate(inflater, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        binding.setViewModel(sharedViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Fix the back arrow's position.
        view.post(() -> {
            final WindowMetrics metrics = requireActivity().getWindowManager().getCurrentWindowMetrics();
            final android.graphics.Insets insets = metrics.getWindowInsets().getInsets(android.view.WindowInsets.Type.statusBars());
            int statusBarHeight = insets.top;
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) binding.buttonBack.getLayoutParams();
            int originalTopMarginInPixels = (int) (16 * getResources().getDisplayMetrics().density);
            layoutParams.topMargin = originalTopMarginInPixels + statusBarHeight;
            binding.buttonBack.setLayoutParams(layoutParams);
        });

        final NavController navController = Navigation.findNavController(view);

        // Set up the back button navigation
        binding.buttonBack.setOnClickListener(v -> navController.navigateUp());

        // When the profile image is clicked, launch the image picker.
        binding.imageViewProfile.setOnClickListener(v -> {
            getContentLauncher.launch("image/*");
        });

        // --- START: ADDED CLICK LISTENER FOR SAVE BUTTON ---
        binding.buttonSave.setOnClickListener(v -> {
            // Because we are using two-way data binding (@={...}), the ViewModel
            // is already updated with the latest text from the EditTexts.
            // All we need to do is navigate back.
            // Later, you could add database saving logic here.
            navController.navigateUp();
        });
        // --- END: ADDED CLICK LISTENER FOR SAVE BUTTON ---
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}