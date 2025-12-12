package com.example.bookcare_qy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class Add_New_Book extends Fragment {

    public Add_New_Book() { }

    public static Add_New_Book newInstance(String param1, String param2) {
        Add_New_Book fragment = new Add_New_Book();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add__new__book, container, false);

        // --- BACK BUTTON ---
        ImageButton btnBack = view.findViewById(R.id.BtnLeftArrow);
        Button btnCancel = view.findViewById(R.id.BtnCancel);

        View.OnClickListener goHome = v -> getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();

        btnBack.setOnClickListener(goHome);
        btnCancel.setOnClickListener(goHome);

        // Card layouts + radio buttons + labels
        ConstraintLayout cardExchange = view.findViewById(R.id.cardExchange);
        ConstraintLayout cardDonate = view.findViewById(R.id.cardDonate);
        RadioButton rbExchange = cardExchange.findViewById(R.id.rbMain);
        RadioButton rbDonate = cardDonate.findViewById(R.id.rbMain);
        TextView tvExchangeTitle = cardExchange.findViewById(R.id.tvTitle);
        TextView tvExchangeSubtitle = cardExchange.findViewById(R.id.tvSubtitle);
        TextView tvDonateTitle = cardDonate.findViewById(R.id.tvTitle);
        TextView tvDonateSubtitle = cardDonate.findViewById(R.id.tvSubtitle);

        tvExchangeTitle.setText("Exchange");
        tvExchangeSubtitle.setText("Trade with another book");
        tvDonateTitle.setText("Donate");
        tvDonateSubtitle.setText("Give away for free");

        rbExchange.setChecked(true); // default

        // Make the whole card clickable
        cardExchange.setOnClickListener(v -> {
            rbExchange.setChecked(true);
            rbDonate.setChecked(false);
        });

        cardDonate.setOnClickListener(v -> {
            rbDonate.setChecked(true);
            rbExchange.setChecked(false);
        });


        // Add book button
        Button btnAddBook = view.findViewById(R.id.BtnListBook);
        btnAddBook.setOnClickListener(v -> {
            String title = ((EditText) view.findViewById(R.id.ETBookTitle)).getText().toString();
            String author = ((EditText) view.findViewById(R.id.ETAuthor)).getText().toString();
            String status = rbExchange.isChecked() ? "Exchange" : "Donate";

            // send book info to My_Listings
            Bundle result = new Bundle();
            result.putString("title", title);
            result.putString("author", author);
            result.putString("status", status);
            getParentFragmentManager().setFragmentResult("newBook", result);
            BookRepository.addBook(new Book(title, author, status, 0, 0, "You"));

            // Go to BookAddedFragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, BookAddedFragment.newInstance("", ""))
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}