package com.example.bookcare_qy;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.example.bookcare_qy.SharedViewModel;
import com.example.bookcare_qy.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private SharedViewModel sharedViewModel;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("ThemePrefs", Context.MODE_PRIVATE);

        binding.setViewModel(sharedViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);

        binding.rowEditProfile.setOnClickListener(v -> navController.navigate(R.id.action_navigation_settings_to_profileEditFragment));
        binding.cardUserProfile.setOnClickListener(v -> navController.navigate(R.id.action_navigation_settings_to_profileViewFragment));
        binding.rowMyBadges.setOnClickListener(v -> navController.navigate(R.id.action_navigation_settings_to_myBadgesFragment));

        binding.rowLogout.setOnClickListener(v -> {
            NavGraph rootGraph = navController.getGraph();
            while (rootGraph.getParent() != null) {
                rootGraph = rootGraph.getParent();
            }

            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(rootGraph.getId(), true)
                    .build();
            navController.navigate(rootGraph.getStartDestinationId(), null, navOptions);

            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
        });

        // --- START: BLUE MODE LOGIC ---
        boolean isBlueMode = sharedPreferences.getBoolean("isBlueMode", false);
        binding.switchBlueMode.setChecked(isBlueMode);

        binding.switchBlueMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isBlueMode", isChecked);
            editor.apply();
            requireActivity().recreate();
        });
        // --- END: BLUE MODE LOGIC ---
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
