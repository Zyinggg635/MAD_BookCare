package com.example.bookcare_qy;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class ViewBookDetailFragment extends Fragment {

    private Book book;

    public ViewBookDetailFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            book = getArguments().getParcelable("book");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_book_detail, container, false);

        // Back button
        LinearLayout backNav = view.findViewById(R.id.back_navigation_area);
        backNav.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        // Populate book data
        if (book != null) {
            TextView tvTitle = view.findViewById(R.id.book_title);
            TextView tvAuthor = view.findViewById(R.id.book_author);
            TextView tvCondition = view.findViewById(R.id.condition_tag);
            TextView tvDescription = view.findViewById(R.id.book_description);
            TextView tvOwnerName = view.findViewById(R.id.owner_name);
            TextView tvOwnerPhone = view.findViewById(R.id.owner_phone_number);
            Button btnRequest = view.findViewById(R.id.bottom_action_button);

            tvTitle.setText(book.getTitle() != null ? book.getTitle() : "Unknown Title");
            tvAuthor.setText(book.getAuthor() != null ? "by " + book.getAuthor() : "by Unknown Author");

            String conditionText = book.getCondition() != null && !book.getCondition().isEmpty()
                    ? book.getCondition() : (book.getGenre() != null ? book.getGenre() : "Available");
            tvCondition.setText(conditionText);

            String description = "A great book in excellent condition. Perfect for reading and sharing with others.";
            tvDescription.setText(description);

            String ownerName = "Unknown User";
            tvOwnerName.setText(ownerName);
            tvOwnerPhone.setText("+60 12-345 6789"); // Default phone, can be extended later

            if ("Donate".equalsIgnoreCase(book.getGenre())) {
                btnRequest.setText("Request Donation");
            } else {
                btnRequest.setText("Request Exchange");
            }

            btnRequest.setOnClickListener(v -> {
                if ("Exchange".equalsIgnoreCase(book.getGenre())) {
                    handleExchangeRequest();
                } else {
                    handleDonationRequest();
                }
            });
        }

        return view;
    }

    private void handleExchangeRequest() {
        CreditManager creditManager = new CreditManager(requireContext());

        if (!creditManager.hasEnoughCredits(1)) {
            showInsufficientCreditsDialog();
            return;
        }

        if (creditManager.deductCredits(1)) {
            showExchangeDoneDialog();
        } else {
            showInsufficientCreditsDialog();
        }
    }

    private void handleDonationRequest() {
        showExchangeDoneDialog();
    }

    private void showInsufficientCreditsDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_request_sent_pop_up);

        Window window = dialog.getWindow();
        if (window != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            window.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        View btnClose = dialog.findViewById(R.id.btn_close);
        TextView tvTitle = dialog.findViewById(R.id.text_title);
        TextView tvMessage = dialog.findViewById(R.id.text_message);
        tvTitle.setText("Insufficient Credits");

        CreditManager creditManager = new CreditManager(requireContext());
        int currentCredits = creditManager.getCredits();
        tvMessage.setText("You need 1 credit to request an exchange. You currently have " +
                currentCredits + " credit(s). Add a book for exchange to earn credits!");

        btnClose.setOnClickListener(v -> dialog.dismiss());
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void showExchangeDoneDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_request_sent_pop_up);

        Window window = dialog.getWindow();
        if (window != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            window.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        View btnClose = dialog.findViewById(R.id.btn_close);
        TextView tvTitle = dialog.findViewById(R.id.text_title);
        TextView tvMessage = dialog.findViewById(R.id.text_message);
        tvTitle.setText("Exchange Done");
        tvMessage.setText("This exchange will post to the forum.");

        btnClose.setOnClickListener(v -> {
            dialog.dismiss();
            Navigation.findNavController(requireView()).navigateUp();
        });
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}
