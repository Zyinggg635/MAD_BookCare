package com.example.bookcare_qy;

import android.content.Intent;
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

import com.example.bookcare_qy.databinding.FragmentEcoPointsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EcoPointsFragment extends Fragment {

    private FragmentEcoPointsBinding binding;
    private DatabaseReference userRef;
    private ValueEventListener userEventListener;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentEcoPointsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Handle system bar insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom
            );
            return insets;
        });

        // Navigation back button
        NavController navController = Navigation.findNavController(view);
        binding.backButton.setOnClickListener(v -> navController.navigateUp());

        // Book Exchange Box Click Listener
        binding.bookExchangeBox.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), activity_book_exchange.class);
            startActivity(intent);
        });

        // Book Donation Box Click Listener
        binding.bookDonationBox.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), activity_book_donation.class);
            startActivity(intent);
        });

        // View History Button Click Listener
        binding.btnViewHistory.setOnClickListener(v -> 
            navController.navigate(R.id.action_ecoPointsFragment_to_ecoPointsHistoryFragment));
        
        // Load user points and display calculations
        loadUserPointsAndCalculate();
    }
    
    private void loadUserPointsAndCalculate() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;
        
        userRef = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL)
                .getReference(Constants.PATH_USERS)
                .child(currentUser.getUid());
        
        userEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    int totalPoints = user.getTotalPoints();
                    int booksExchanged = user.getBooksExchanged();
                    
                    // Display total points
                    binding.totalPointsValue.setText(String.valueOf(totalPoints));
                    
                    // Calculate and display exchange details
                    // Each exchange = 5 points, so: exchanges * 5 = total from exchanges
                    // But total points might include donations too, so we show based on booksExchanged count
                    if (binding.textView13 != null) {
                        binding.textView13.setText(booksExchanged + " exchanges");
                    }
                    
                    // Calculate points from exchanges (booksExchanged * 5)
                    int pointsFromExchanges = booksExchanged * Constants.POINTS_PER_BOOK_EXCHANGE;
                    // Note: This shows points from exchanges only, totalPoints might include donations
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        };
        userRef.addValueEventListener(userEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (userRef != null && userEventListener != null) {
            userRef.removeEventListener(userEventListener);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
