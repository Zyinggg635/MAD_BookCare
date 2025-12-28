package com.example.bookcare_qy;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class My_Listings extends Fragment {

    private static final String TAG = "My_Listings";
    private BookAdapter adapter;
    private TextView tvSubTitle;
    private List<Book> myListings = new ArrayList<>();

    private DatabaseReference booksRef;
    private ValueEventListener valueEventListener;

    public My_Listings() { }

    public static My_Listings newInstance(String param1, String param2) {
        return new My_Listings();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        booksRef = FirebaseDatabase.getInstance("https://bookcare-82eb6-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("books");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my__listings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvSubTitle = view.findViewById(R.id.tvSubTitle);
        ImageButton btnBack = view.findViewById(R.id.BtnLeftArrow);
        Button btnAddBook = view.findViewById(R.id.btnAddBook);
        RecyclerView recyclerView = view.findViewById(R.id.rvBooks);

        final NavController navController = Navigation.findNavController(view);

        btnBack.setOnClickListener(v -> navController.navigateUp());
        btnAddBook.setOnClickListener(v -> navController.navigate(R.id.action_my_Listings_to_add_New_Book));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BookAdapter(myListings, count -> updateSubtitle());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Attaching ValueEventListener");

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myListings.clear();
                for (DataSnapshot bookSnapshot : snapshot.getChildren()) {
                    Book book = bookSnapshot.getValue(Book.class);
                    if (book != null) {
                        myListings.add(book);
                    }
                }
                adapter.notifyDataSetChanged();
                updateSubtitle();
                Log.d(TAG, "onDataChange: Fetched " + myListings.size() + " books.");
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

    private void updateSubtitle() {
        if (tvSubTitle != null) {
            tvSubTitle.setText(myListings.size() + " books listed");
        }
    }
}
