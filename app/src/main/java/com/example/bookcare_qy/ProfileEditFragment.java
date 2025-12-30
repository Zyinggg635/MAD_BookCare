package com.example.bookcare_qy;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.bookcare_qy.databinding.FragmentProfileEditBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileEditFragment extends Fragment {

    private FragmentProfileEditBinding binding;
    private SharedViewModel sharedViewModel;

    private final ActivityResultLauncher<String> getContentLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    sharedViewModel.profileImageUri.setValue(uri);
                }
            });

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentProfileEditBinding.inflate(inflater, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        binding.setViewModel(sharedViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        // ✅ FIXED: Back arrow position (compatible with all Android versions)
        view.post(() -> {
            int statusBarHeight = 0;
            int resourceId = getResources().getIdentifier(
                    "status_bar_height", "dimen", "android"
            );
            if (resourceId > 0) {
                statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            }

            ConstraintLayout.LayoutParams layoutParams =
                    (ConstraintLayout.LayoutParams) binding.buttonBack.getLayoutParams();

            int originalTopMarginInPixels =
                    (int) (16 * getResources().getDisplayMetrics().density);

            layoutParams.topMargin = originalTopMarginInPixels + statusBarHeight;
            binding.buttonBack.setLayoutParams(layoutParams);
        });

        NavController navController = Navigation.findNavController(view);

        // Back button
        binding.buttonBack.setOnClickListener(v -> navController.navigateUp());

        // Pick profile image
        binding.imageViewProfile.setOnClickListener(v ->
                getContentLauncher.launch("image/*")
        );

        // Load user data
        loadUserDataFromFirebase();

        // Save button
        binding.buttonSave.setOnClickListener(v -> {
            saveUserProfileToFirebase();
            navController.navigateUp();
        });
    }

    private void saveUserProfileToFirebase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        DatabaseReference userRef =
                FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL)
                        .getReference(Constants.PATH_USERS)
                        .child(currentUser.getUid());

        String name = sharedViewModel.name.getValue();
        String age = sharedViewModel.age.getValue();
        String bio = sharedViewModel.bio.getValue();
        String phone = sharedViewModel.phone.getValue();
        String location = sharedViewModel.location.getValue();

        if (name != null) userRef.child("username").setValue(name);
        if (age != null) userRef.child("age").setValue(age);
        if (bio != null) userRef.child("bio").setValue(bio);
        if (phone != null) userRef.child("phone").setValue(phone);
        if (location != null) userRef.child("address").setValue(location);
    }

    private void loadUserDataFromFirebase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        DatabaseReference userRef =
                FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL)
                        .getReference(Constants.PATH_USERS)
                        .child(currentUser.getUid());

        userRef.addListenerForSingleValueEvent(
                new com.google.firebase.database.ValueEventListener() {

                    @Override
                    public void onDataChange(
                            @NonNull com.google.firebase.database.DataSnapshot snapshot
                    ) {
                        User user = snapshot.getValue(User.class);
                        if (user != null) {
                            if (user.getUsername() != null)
                                sharedViewModel.name.setValue(user.getUsername());
                            if (user.getEmail() != null)
                                sharedViewModel.email.setValue(user.getEmail());
                            if (user.getPhone() != null)
                                sharedViewModel.phone.setValue(user.getPhone());
                            if (user.getAddress() != null)
                                sharedViewModel.location.setValue(user.getAddress());
                            if (user.getAge() != null)
                                sharedViewModel.age.setValue(user.getAge());
                            if (user.getBio() != null)
                                sharedViewModel.bio.setValue(user.getBio());
                        }
                    }

                    @Override
                    public void onCancelled(
                            @NonNull com.google.firebase.database.DatabaseError error
                    ) {
                        // Handle error if needed
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
