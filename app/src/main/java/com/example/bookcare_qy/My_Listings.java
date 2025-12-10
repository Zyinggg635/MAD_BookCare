package com.example.bookcare_qy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class My_Listings extends Fragment {

    // Make books static so the list persists across fragment recreation
    private static ArrayList<Book> books = new ArrayList<>();
    private BookAdapter adapter;
    private TextView tvSubTitle;

    public My_Listings() { }

    public static My_Listings newInstance(String param1, String param2) {
        My_Listings fragment = new My_Listings();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my__listings, container, false);

        tvSubTitle = view.findViewById(R.id.tvSubTitle);
        updateSubtitle();

        ImageButton btnBack = view.findViewById(R.id.BtnLeftArrow);
        btnBack.setOnClickListener(v -> getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit());

        Button btnAddBook = view.findViewById(R.id.btnAddBook);
        btnAddBook.setOnClickListener(v -> getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, Add_New_Book.newInstance("", ""))
                .addToBackStack(null)
                .commit());

        RecyclerView recyclerView = view.findViewById(R.id.rvBooks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new BookAdapter(books, count -> updateSubtitle());
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getParentFragmentManager().setFragmentResultListener("newBook", getViewLifecycleOwner(), (requestKey, bundle) -> {
            String title = bundle.getString("title");
            String author = bundle.getString("author");
            String status = bundle.getString("status");

            Book newBook = new Book(title, author, status, 0, 0);
            books.add(newBook);
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
            updateSubtitle();
        });
    }

    private void updateSubtitle() {
        if (tvSubTitle != null) {
            tvSubTitle.setText(books.size() + " books listed");
        }
    }
}