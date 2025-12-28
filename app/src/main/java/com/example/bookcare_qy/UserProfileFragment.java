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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserProfileFragment extends Fragment {

    private com.example.bookcare_qy.databinding.FragmentUserProfileBinding binding;
    private SharedViewModel sharedViewModel;
    private RecommendationAdapter adapter;
    private DatabaseReference booksRef;
    private ValueEventListener valueEventListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = com.example.bookcare_qy.databinding.FragmentUserProfileBinding.inflate(inflater, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        binding.setViewModel(sharedViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Handle edge-to-edge insets by adding padding to the root ScrollView.
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });

        final NavController navController = Navigation.findNavController(view);

        binding.cardEcoPoints.setOnClickListener(v ->
                navController.navigate(R.id.action_navigation_home_to_ecoPointsFragment)
        );

        // --- Recommendations Setup ---
        RecyclerView recyclerView = binding.recyclerViewRecommendations;
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        adapter = new RecommendationAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Initialize Firebase Database reference
        booksRef = FirebaseDatabase.getInstance("https://bookcare-82eb6-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("books");
    }

    @Override
    public void onStart() {
        super.onStart();
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Book> allBooks = new ArrayList<>();
                for (DataSnapshot bookSnapshot : snapshot.getChildren()) {
                    Book book = bookSnapshot.getValue(Book.class);
                    if (book != null) {
                        allBooks.add(book);
                    }
                }
                updateRecommendations(allBooks);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        };
        booksRef.addValueEventListener(valueEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (booksRef != null && valueEventListener != null) {
            booksRef.removeEventListener(valueEventListener);
        }
    }

    private void updateRecommendations(List<Book> allBooks) {
        // Placeholder for user's favorite genres
        List<String> userFavoriteGenres = Arrays.asList("Fantasy", "Sci-Fi");

        List<Book> recommendedBooks = new ArrayList<>();
        for (Book book : allBooks) {
            if (book.getGenre() != null && userFavoriteGenres.contains(book.getGenre())) {
                recommendedBooks.add(book);
            }
        }
        adapter.submitList(recommendedBooks);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
