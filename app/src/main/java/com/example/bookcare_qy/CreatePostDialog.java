package com.example.bookcare_qy;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class CreatePostDialog extends DialogFragment {

    private String selectedPostType = "Discussion"; // Default to Discussion
    private Button btnDiscussion, btnReview, btnEvent;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_create_post, null);

        btnDiscussion = view.findViewById(R.id.btnDiscussion);
        btnReview = view.findViewById(R.id.btnReview);
        btnEvent = view.findViewById(R.id.btnEvent);
        final EditText etTitle = view.findViewById(R.id.etPostTitle);
        final EditText etMessage = view.findViewById(R.id.etPostMessage);

        btnDiscussion.setOnClickListener(v -> selectButton(btnDiscussion));
        btnReview.setOnClickListener(v -> selectButton(btnReview));
        btnEvent.setOnClickListener(v -> selectButton(btnEvent));

        selectButton(btnDiscussion);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(view);

        builder.setPositiveButton("Post", (d, id) -> {
            String title = etTitle.getText().toString();
            String message = etMessage.getText().toString();
            ForumActivity activity = (ForumActivity) getActivity();
            if (activity != null) {
                activity.createNewPost(selectedPostType, title, message);
            }
        });
        builder.setNegativeButton("Cancel", (d, id) -> dismiss());

        Dialog dialog = builder.create();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setGravity(Gravity.BOTTOM);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        return dialog;
    }

    private void selectButton(Button selectedButton) {
        btnDiscussion.setAlpha(1.0f);
        btnReview.setAlpha(1.0f);
        btnEvent.setAlpha(1.0f);
        selectedButton.setAlpha(0.7f);
        selectedPostType = selectedButton.getText().toString();
    }
}
