package com.example.bookcare_qy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowMetrics;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.example.bookcare_qy.SharedViewModel;
import com.example.bookcare_qy.databinding.FragmentProfileViewBinding;

public class ProfileViewFragment extends Fragment {

    private FragmentProfileViewBinding binding;
    private SharedViewModel sharedViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileViewBinding.inflate(inflater, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        binding.setViewModel(sharedViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // This block fixes the back arrow's position.
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
        binding.buttonBack.setOnClickListener(v -> navController.navigateUp());

        binding.buttonLogout.setOnClickListener(v -> {
            // Programmatically find the root graph and navigate to its start destination
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
