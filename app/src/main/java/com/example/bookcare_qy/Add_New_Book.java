package com.example.bookcare_qy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

//testing
//wheweree i pushh
public class Add_New_Book extends Fragment {


    private BookRepository bookRepository;

    public Add_New_Book() { }

    public static Add_New_Book newInstance(String param1, String param2) {
        return new Add_New_Book();
    }

    public static Add_New_Book newExchangeOnlyInstance() {
        Add_New_Book fragment = new Add_New_Book();
        Bundle args = new Bundle();
        args.putBoolean("forceExchange", true);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add__new__book, container, false);

        bookRepository = new BookRepository();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final boolean forceExchange = getArguments() != null
                && getArguments().getBoolean("forceExchange", false);

        NavController navController = Navigation.findNavController(view);

        // --- BACK BUTTON ---
        ImageButton btnBack = view.findViewById(R.id.BtnLeftArrow);
        Button btnCancel = view.findViewById(R.id.BtnCancel);

        View.OnClickListener goHome = v -> navController.navigateUp();

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
        if (forceExchange) {
            rbDonate.setEnabled(false);
            cardDonate.setEnabled(false);
            cardDonate.setAlpha(0.4f);
        }

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
            String status = forceExchange ? "Exchange" : (rbExchange.isChecked() ? "Exchange" : "Donate");

            Spinner conditionSpinner = view.findViewById(R.id.SPCondition);
            String condition = conditionSpinner.getSelectedItem() != null ? conditionSpinner.getSelectedItem().toString() : "";

            Book newBook = new Book(null, title, author, condition, status);
            bookRepository.addBook(newBook);

            // Go to BookAddedFragment
            navController.navigate(R.id.action_add_New_Book_to_bookAddedFragment);
        });
    }
}
