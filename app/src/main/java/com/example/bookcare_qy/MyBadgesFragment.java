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

import java.util.ArrayList;
import java.util.List;

public class MyBadgesFragment extends Fragment {

    private BadgeAdapter badgeAdapter;
    private List<Badge> allBadges = new ArrayList<>();

    // Placeholder for user's current points
    private int currentUserPoints = 85;

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

        // Calculate and display everything
        calculateAndDisplayProgression(tvCurrentLevel, tvLevelNumber, tvTotalPoints, pbLevelProgress, tvProgressText, tvBadgesHeader);
    }

    private void initializeBadges() {
        allBadges.add(new Badge("Bronze", "Level 1-10", 10));
        allBadges.add(new Badge("Silver", "Level 11-20", 100));
        allBadges.add(new Badge("Gold", "Level 21-30", 250));
        allBadges.add(new Badge("Platinum", "Level 31-40", 500));
        // Add more badges as needed
    }

    private void calculateAndDisplayProgression(TextView tvCurrentLevel, TextView tvLevelNumber, TextView tvTotalPoints, ProgressBar pbLevelProgress, TextView tvProgressText, TextView tvBadgesHeader) {
        Badge currentBadge = null;
        Badge nextBadge = null;
        int level = 1 + (currentUserPoints / 10); // Example: 10 points per level

        for (Badge badge : allBadges) {
            if (currentUserPoints >= badge.getPointsRequired()) {
                currentBadge = badge;
            } else {
                nextBadge = badge;
                break;
            }
        }

        // Update top section
        if (currentBadge != null) {
            tvCurrentLevel.setText(currentBadge.getName());
            tvBadgesHeader.setText(currentBadge.getName());
        } else {
            tvCurrentLevel.setText("Beginner");
            tvBadgesHeader.setText("Bronze Tier");
        }
        
        tvLevelNumber.setText("Level " + level);
        tvTotalPoints.setText(currentUserPoints + " total points");

        // Update progress bar
        if (nextBadge != null) {
            int pointsForLastBadge = (currentBadge != null) ? currentBadge.getPointsRequired() : 0;
            int progress = currentUserPoints - pointsForLastBadge;
            int totalNeeded = nextBadge.getPointsRequired() - pointsForLastBadge;
            
            pbLevelProgress.setMax(totalNeeded);
            pbLevelProgress.setProgress(progress);
            tvProgressText.setText((totalNeeded - progress) + " points to " + nextBadge.getName());
        } else {
            // User has maxed out
            pbLevelProgress.setProgress(pbLevelProgress.getMax());
            tvProgressText.setText("You have reached the highest level!");
        }

        // The adapter already has the list of all badges, so we just need to notify it.
        // The adapter itself will handle graying out unearned badges.
        badgeAdapter.updateUserPoints(currentUserPoints);
    }
}
