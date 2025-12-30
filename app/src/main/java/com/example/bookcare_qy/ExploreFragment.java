package com.example.bookcare_qy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.bookcare_qy.databinding.FragmentExploreBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExploreFragment extends Fragment {

    private FragmentExploreBinding binding;
    private HomeBookAdapter bookAdapter;
    private List<Book> allBooks = new ArrayList<>();
    private List<Book> exchangeBooks = new ArrayList<>();
    private List<Book> donationBooks = new ArrayList<>();
    private DatabaseReference booksRef;
    private ValueEventListener valueEventListener;
    private boolean showingExchange = true; // Track current view

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentExploreBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);

        binding.btnMyListings.setOnClickListener(v ->
                navController.navigate(R.id.action_navigation_explore_to_my_Listings));
        binding.btnUploadBook.setOnClickListener(v ->
                navController.navigate(R.id.action_navigation_explore_to_add_New_Book));

        setupRecyclerView();
        booksRef = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference(Constants.PATH_BOOKS);

        binding.btnExchange.setOnClickListener(v -> {
            showingExchange = true;
            showExchangeBooks();
        });
        binding.btnDonate.setOnClickListener(v -> {
            showingExchange = false;
            showDonationBooks();
        });
        
        // Setup search functionality
        binding.etSearch.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterBooks(s.toString());
            }
            
            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void setupRecyclerView() {
        bookAdapter = new HomeBookAdapter(new ArrayList<>());
        binding.rvHomeBooks.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rvHomeBooks.setAdapter(bookAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allBooks.clear();
                for (DataSnapshot bookSnapshot : snapshot.getChildren()) {
                    Book book = bookSnapshot.getValue(Book.class);
                    if (book != null) {
                        allBooks.add(book);
                    }
                }
                filterBookLists();
                showExchangeBooks(); // Show exchange books by default
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle potential errors
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

    private void filterBookLists() {
        exchangeBooks = allBooks.stream()
                .filter(book -> "Exchange".equalsIgnoreCase(book.getListingType()))
                .collect(Collectors.toList());

        donationBooks = allBooks.stream()
                .filter(book -> "Donation".equalsIgnoreCase(book.getListingType()))
                .collect(Collectors.toList());
    }

    private void showExchangeBooks() {
        bookAdapter.updateBooks(exchangeBooks);
        binding.tvSectionTitle.setText("Available for Exchange");
    }

    private void showDonationBooks() {
        bookAdapter.updateBooks(donationBooks);
        binding.tvSectionTitle.setText("Available for Donation");
    }
    
    private void filterBooks(String searchQuery) {
        if (searchQuery.isEmpty()) {
            // Show current category (exchange or donation)
            if (showingExchange) {
                showExchangeBooks();
            } else {
                showDonationBooks();
            }
            return;
        }
        
        String query = searchQuery.toLowerCase().trim();
        List<Book> filteredBooks = new ArrayList<>();
        
        List<Book> sourceList = showingExchange ? exchangeBooks : donationBooks;
        for (Book book : sourceList) {
            boolean matches = false;
            if (book.getTitle() != null && book.getTitle().toLowerCase().contains(query)) {
                matches = true;
            } else if (book.getAuthor() != null && book.getAuthor().toLowerCase().contains(query)) {
                matches = true;
            } else if (book.getGenre() != null && book.getGenre().toLowerCase().contains(query)) {
                matches = true;
            }
            
            if (matches) {
                filteredBooks.add(book);
            }
        }
        
        bookAdapter.updateBooks(filteredBooks);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
