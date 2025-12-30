package com.example.bookcare_qy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Add_New_Book extends Fragment {

    private BookRepository bookRepository;

    public Add_New_Book() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add__new__book, container, false);
        bookRepository = new BookRepository();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final boolean forceExchange = getArguments() != null && getArguments().getBoolean("forceExchange", false);
        NavController navController = Navigation.findNavController(view);

        ImageButton btnBack = view.findViewById(R.id.BtnLeftArrow);
        Button btnCancel = view.findViewById(R.id.BtnCancel);
        View.OnClickListener goHome = v -> navController.navigateUp();
        btnBack.setOnClickListener(goHome);
        btnCancel.setOnClickListener(goHome);

        RadioGroup radioGroupListing = view.findViewById(R.id.radioGroupListing);
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

        // Track selection state manually (since RadioButtons are in included layouts)
        final boolean[] isExchangeSelected = {true}; // Start with Exchange selected

        // Set initial selection
        rbExchange.setChecked(true);
        rbDonate.setChecked(false);
        if (forceExchange) {
            rbDonate.setEnabled(false);
            cardDonate.setClickable(false);
            cardDonate.setAlpha(0.4f);
        }

        // Handle card clicks to toggle radio buttons
        cardExchange.setOnClickListener(v -> {
            if (!forceExchange) {
                isExchangeSelected[0] = true;
                rbExchange.setChecked(true);
                rbDonate.setChecked(false);
            }
        });

        cardDonate.setOnClickListener(v -> {
            if (!forceExchange) {
                isExchangeSelected[0] = false;
                rbDonate.setChecked(true);
                rbExchange.setChecked(false);
            }
        });
        
        // Store selection state for later use
        final boolean[] finalSelection = isExchangeSelected;

        Button btnAddBook = view.findViewById(R.id.BtnListBook);
        btnAddBook.setOnClickListener(v -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser == null) {
                Toast.makeText(getContext(), "You must be logged in to add a book.", Toast.LENGTH_SHORT).show();
                return;
            }
            String ownerId = currentUser.getUid();

            String title = ((EditText) view.findViewById(R.id.ETBookTitle)).getText().toString();
            String author = ((EditText) view.findViewById(R.id.ETAuthor)).getText().toString();
            
            // Get selected listing type
            String listingType;
            if (forceExchange) {
                listingType = "Exchange";
            } else if (finalSelection[0]) {
                listingType = "Exchange";
            } else {
                listingType = "Donation";
            }
            
            // If Exchange, add credit to user
            if ("Exchange".equals(listingType)) {
                addCreditToUser(currentUser.getUid());
            }

            Spinner conditionSpinner = view.findViewById(R.id.SPCondition);
            String condition = conditionSpinner.getSelectedItem() != null ? conditionSpinner.getSelectedItem().toString() : "";

            Spinner genreSpinner = view.findViewById(R.id.SPGenre);
            String genre = genreSpinner.getSelectedItem() != null ? genreSpinner.getSelectedItem().toString() : "";

            Book newBook = new Book(null, title, author, condition, listingType, genre, ownerId);
            bookRepository.addBook(newBook);

            navController.navigate(R.id.action_add_New_Book_to_bookAddedFragment);
        });
    }
    
    private void addCreditToUser(String userId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL)
                .getReference(Constants.PATH_USERS)
                .child(userId)
                .child("credits");
        
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Integer currentCredits = task.getResult().getValue(Integer.class);
                if (currentCredits == null) currentCredits = 0;
                userRef.setValue(currentCredits + 1);
            }
        });
    }
}
