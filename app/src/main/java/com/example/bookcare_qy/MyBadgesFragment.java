package com.example.bookcare_qy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyBadgesFragment extends Fragment {

    private BadgeAdapter badgeAdapter;
    private List<Badge> allBadges = new ArrayList<>();

    private int currentUserPoints = 0;
    private DatabaseReference userRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeBadges();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_badges, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views from the new layout
        TextView tvCurrentLevel = view.findViewById(R.id.tvCurrentLevel);
        TextView tvLevelNumber = view.findViewById(R.id.tvLevelNumber);
        TextView tvTotalPoints = view.findViewById(R.id.tvTotalPoints);
        ProgressBar pbLevelProgress = view.findViewById(R.id.pbLevelProgress);
        TextView tvProgressText = view.findViewById(R.id.tvProgressText);
        TextView tvBadgesHeader = view.findViewById(R.id.tvBadgesHeader);
        RecyclerView rvBadges = view.findViewById(R.id.rvBadges);
        ImageButton btnBack = view.findViewById(R.id.btn_back);

        // Setup RecyclerView with a GridLayoutManager
        badgeAdapter = new BadgeAdapter(allBadges);
        rvBadges.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvBadges.setAdapter(badgeAdapter);

        // Handle back button click
        btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        // Load user points from Firebase
        loadUserPoints(tvCurrentLevel, tvLevelNumber, tvTotalPoints, pbLevelProgress, tvProgressText, tvBadgesHeader);
    }
    
    private void loadUserPoints(TextView tvCurrentLevel, TextView tvLevelNumber, TextView tvTotalPoints, 
                                ProgressBar pbLevelProgress, TextView tvProgressText, TextView tvBadgesHeader) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            calculateAndDisplayProgression(tvCurrentLevel, tvLevelNumber, tvTotalPoints, pbLevelProgress, tvProgressText, tvBadgesHeader);
            return;
        }
        
        userRef = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL)
                .getReference(Constants.PATH_USERS)
                .child(currentUser.getUid());
        
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    // SIMULATION: Use calculated points based on simulated counts (3 exchanges, 5 donations)
                    // This matches the simulation in EcoPointsFragment, UserProfileFragment, and detail activities.
                    
                    int simulatedExchanges = 3;
                    int simulatedDonations = 5;
                    currentUserPoints = (simulatedExchanges * Constants.POINTS_PER_BOOK_EXCHANGE) + 
                                        (simulatedDonations * Constants.POINTS_PER_BOOK_DONATION);
                    
                    // To use real database value, revert to:
                    // currentUserPoints = user.getTotalPoints();
                    
                    // Update badge level (50 points per level, starting at 0)
                    int badgeLevel = currentUserPoints / 50;
                    
                    // Only update Firebase if we were using real data. With simulation, we just display it locally.
                    // userRef.child("badgeLevel").setValue(badgeLevel); 
                    
                    calculateAndDisplayProgression(tvCurrentLevel, tvLevelNumber, tvTotalPoints, pbLevelProgress, tvProgressText, tvBadgesHeader);
                    badgeAdapter.updateUserPoints(currentUserPoints);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                calculateAndDisplayProgression(tvCurrentLevel, tvLevelNumber, tvTotalPoints, pbLevelProgress, tvProgressText, tvBadgesHeader);
            }
        });
    }

    private void initializeBadges() {
        for (int i = 0; i <= 50; i++) { // Levels 0 to 50 (50 points per level)
            allBadges.add(new Badge("Level " + i, "", i * 50, i));
        }
    }

    private void calculateAndDisplayProgression(TextView tvCurrentLevel, TextView tvLevelNumber, TextView tvTotalPoints, ProgressBar pbLevelProgress, TextView tvProgressText, TextView tvBadgesHeader) {
        // Badge level: 50 points per level, starting at level 0
        int userLevel = currentUserPoints / 50;
        int pointsForCurrentLevel = userLevel * 50;
        int pointsForNextLevel = (userLevel + 1) * 50;
        int progress = currentUserPoints - pointsForCurrentLevel;

        tvCurrentLevel.setText("Level " + userLevel);
        tvBadgesHeader.setText("All Badges");
        tvLevelNumber.setText("Level " + userLevel);
        tvTotalPoints.setText(currentUserPoints + " total points");

        if (pointsForNextLevel > 0) { // Not max level
            pbLevelProgress.setMax(50); // 50 points per level
            pbLevelProgress.setProgress(progress);
            tvProgressText.setText(progress + " / 50 points to Level " + (userLevel + 1));
        } else {
            pbLevelProgress.setMax(50);
            pbLevelProgress.setProgress(50);
            tvProgressText.setText("You have reached the highest level!");
        }
    }
}
