package com.example.bookcare_qy; // Make sure this matches your package name

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment"; // For logging

    private DatabaseReference booksRef;
    private ValueEventListener valueEventListener;
    private List<Book> allBooks = new ArrayList<>();
    private HomeBookAdapter adapter;
    private String currentFilter = "Exchange";
    private EditText etSearch;
    private TextView tvSectionTitle;
    private TextView tvEmptyState;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false); // Your fragment layout

        // Initialize Firebase Database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://bookcare-82eb6-default-rtdb.asia-southeast1.firebasedatabase.app");
        booksRef = database.getReference("books");

        // --- Find Views ---
        ImageButton btnBack = view.findViewById(R.id.btnBack);
        View btnUploadBook = view.findViewById(R.id.btnUploadBook);
        View btnMyListings = view.findViewById(R.id.btnMyListings);
        Button btnExchange = view.findViewById(R.id.btnExchange);
        Button btnDonate = view.findViewById(R.id.btnDonate);
        etSearch = view.findViewById(R.id.etSearch);
        tvSectionTitle = view.findViewById(R.id.tvSectionTitle);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);

        // --- Setup Listeners ---
        btnBack.setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
        btnUploadBook.setOnClickListener(v -> getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, Add_New_Book.newInstance("", ""))
                .addToBackStack(null).commit());
        btnMyListings.setOnClickListener(v -> getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new My_Listings())
                .addToBackStack(null).commit());

        btnExchange.setOnClickListener(v -> {
            currentFilter = "Exchange";
            updateToggleButtons(btnExchange, btnDonate);
            filterAndDisplayBooks();
        });

        btnDonate.setOnClickListener(v -> {
            currentFilter = "Donate";
            updateToggleButtons(btnExchange, btnDonate);
            filterAndDisplayBooks();
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override public void afterTextChanged(Editable s) {
                filterAndDisplayBooks();
            }
        });

        // --- RecyclerView Setup ---
        RecyclerView recyclerView = view.findViewById(R.id.rvHomeBooks);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new HomeBookAdapter(this);
        recyclerView.setAdapter(adapter);

        updateToggleButtons(btnExchange, btnDonate);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Attaching ValueEventListener");

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
                Log.d(TAG, "onDataChange: Fetched " + allBooks.size() + " books.");
                filterAndDisplayBooks(); // Display initial data
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "onCancelled: Failed to read value.", error.toException());
            }
        };

        booksRef.addValueEventListener(valueEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: Removing ValueEventListener");
        if (booksRef != null && valueEventListener != null) {
            booksRef.removeEventListener(valueEventListener);
        }
    }

    private void filterAndDisplayBooks() {
        List<Book> filteredList = new ArrayList<>();
        String query = etSearch.getText().toString().toLowerCase();

        for (Book book : allBooks) {
            boolean matchesFilter = book.getType().equalsIgnoreCase(currentFilter);
            boolean matchesQuery = book.getTitle().toLowerCase().contains(query) ||
                                   book.getAuthor().toLowerCase().contains(query);

            if (matchesFilter && matchesQuery) {
                filteredList.add(book);
            }
        }
        adapter.submitList(filteredList);
        tvEmptyState.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void updateToggleButtons(Button btnExchange, Button btnDonate) {
        boolean exchangeSelected = "Exchange".equalsIgnoreCase(currentFilter);

        btnExchange.setBackgroundColor(exchangeSelected ? ContextCompat.getColor(requireContext(), R.color.purple_500) : Color.LTGRAY);
        btnDonate.setBackgroundColor(!exchangeSelected ? ContextCompat.getColor(requireContext(), R.color.purple_500) : Color.LTGRAY);

        tvSectionTitle.setText(exchangeSelected ? "Available for Exchange" : "Available for Donation");
    }
}
