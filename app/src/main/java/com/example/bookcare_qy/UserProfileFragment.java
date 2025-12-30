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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserProfileFragment extends Fragment implements RecommendationAdapter.OnItemClickListener {

    private com.example.bookcare_qy.databinding.FragmentUserProfileBinding binding;
    private SharedViewModel sharedViewModel;
    private RecommendationAdapter adapter;
    private DatabaseReference booksRef;
    private DatabaseReference userRef;
    private ValueEventListener valueEventListener;
    private ValueEventListener userEventListener;

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
        adapter = new RecommendationAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        // Initialize Firebase Database references
        booksRef = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference(Constants.PATH_BOOKS);
        
        // Load user data from Firebase
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userRef = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL)
                    .getReference(Constants.PATH_USERS)
                    .child(currentUser.getUid());
            loadUserData();
        }
    }
    
    private void loadUserData() {
        if (userRef == null) return;
        
        userEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    // Update SharedViewModel with user data
                    sharedViewModel.name.setValue(user.getUsername() != null ? user.getUsername() : "");
                    sharedViewModel.email.setValue(user.getEmail() != null ? user.getEmail() : "");
                    sharedViewModel.phone.setValue(user.getPhone() != null ? user.getPhone() : "");
                    sharedViewModel.location.setValue(user.getAddress() != null ? user.getAddress() : "");
                    
                    // Age and bio - show only if not empty
                    if (user.getAge() != null && !user.getAge().isEmpty()) {
                        sharedViewModel.age.setValue(user.getAge());
                    } else {
                        sharedViewModel.age.setValue("");
                    }
                    
                    if (user.getBio() != null && !user.getBio().isEmpty()) {
                        sharedViewModel.bio.setValue(user.getBio());
                    } else {
                        sharedViewModel.bio.setValue("");
                    }
                    
                    // Update eco points display
                    if (binding.textViewEcoPointsValue != null) {
                        binding.textViewEcoPointsValue.setText(String.valueOf(user.getTotalPoints()));
                    }
                    
                    // Update recommendations based on user's genre preference
                    if (user.getGenrePreference() != null && !user.getGenrePreference().isEmpty()) {
                        // Will be used in updateRecommendations
                    }
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
        if (userRef != null && userEventListener != null) {
            userRef.removeEventListener(userEventListener);
        }
    }

    private void updateRecommendations(List<Book> allBooks) {
        // Get user's genre preference from SharedViewModel or load from Firebase
        List<String> userFavoriteGenres = new ArrayList<>();
        
        // Try to get from current user data
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userGenreRef = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL)
                    .getReference(Constants.PATH_USERS)
                    .child(currentUser.getUid())
                    .child("genrePreference");
            
            userGenreRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().getValue() != null) {
                    String genrePreference = task.getResult().getValue(String.class);
                    if (genrePreference != null && !genrePreference.isEmpty()) {
                        List<Book> recommendedBooks = new ArrayList<>();
                        for (Book book : allBooks) {
                            if (book.getGenre() != null && book.getGenre().equals(genrePreference)) {
                                recommendedBooks.add(book);
                            }
                        }
                        adapter.submitList(recommendedBooks);
                        return;
                    }
                }
                // If no preference, show all books
                adapter.submitList(allBooks);
            });
        } else {
            // If not logged in, show all books
            adapter.submitList(allBooks);
        }
    }

    @Override
    public void onItemClick(Book book) {
        // Navigate to the book detail fragment
        UserProfileFragmentDirections.ActionNavigationHomeToViewBookDetailFragment action =
                UserProfileFragmentDirections.actionNavigationHomeToViewBookDetailFragment(book);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
