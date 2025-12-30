package com.example.bookcare_qy;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bookcare_qy.databinding.FragmentCommunityBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommunityFragment extends Fragment implements PostAdapter.OnPostInteractionListener {

    private FragmentCommunityBinding binding;
    private PostAdapter postAdapter;
    private List<ForumPost> postsList = new ArrayList<>();
    private DatabaseReference postsRef;
    private ValueEventListener valueEventListener;
    private String selectedPostType = "Discussion";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCommunityBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        postAdapter = new PostAdapter(postsList, this);
        binding.rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvPosts.setAdapter(postAdapter);

        postsRef = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference(Constants.PATH_POSTS);
        
        // Note: Activity feed functionality is now available
        // The activity feed is populated when users donate/exchange books in BookDonationActivity/BookExchangeActivity
        // You can add a separate RecyclerView or tab to display activity feed items if needed

        binding.fabNewPost.setOnClickListener(v -> showCreatePostDialog());
    }

    @Override
    public void onStart() {
        super.onStart();
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postsList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ForumPost post = postSnapshot.getValue(ForumPost.class);
                    if (post != null) {
                        post.postId = postSnapshot.getKey();
                        postsList.add(post);
                    }
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load posts.", Toast.LENGTH_SHORT).show();
            }
        };
        postsRef.addValueEventListener(valueEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (postsRef != null && valueEventListener != null) {
            postsRef.removeEventListener(valueEventListener);
        }
    }

    private void showCreatePostDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_create_post, null);
        builder.setView(dialogView);

        final EditText etPostTitle = dialogView.findViewById(R.id.etPostTitle);
        final EditText etPostMessage = dialogView.findViewById(R.id.etPostMessage);

        final Button btnDiscussion = dialogView.findViewById(R.id.btnDiscussion);
        final Button btnReview = dialogView.findViewById(R.id.btnReview);
        final Button btnEvent = dialogView.findViewById(R.id.btnEvent);

        btnDiscussion.setOnClickListener(v -> selectedPostType = "Discussion");
        btnReview.setOnClickListener(v -> selectedPostType = "Review");
        btnEvent.setOnClickListener(v -> selectedPostType = "Event");

        builder.setTitle("Create a New Post")
                .setPositiveButton("Post", (dialog, id) -> {
                    String title = etPostTitle.getText().toString().trim();
                    String message = etPostMessage.getText().toString().trim();

                    if (!title.isEmpty() && !message.isEmpty()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String posterName = (user != null && user.getDisplayName() != null) ? user.getDisplayName() : "Anonymous";
                        
                        ForumPost newPost = new ForumPost(posterName, selectedPostType, title, message, new Date(), 0);
                        postsRef.push().setValue(newPost);
                    } else {
                        Toast.makeText(getContext(), "Title and message cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onLikeClicked(ForumPost post, int position) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null || post.postId == null) return;

        String userId = currentUser.getUid();
        DatabaseReference postLikesRef = postsRef.child(post.postId).child("likes");

        if (post.likes.containsKey(userId)) {
            postLikesRef.child(userId).removeValue();
        } else {
            postLikesRef.child(userId).setValue(true);
        }
    }

    @Override
    public void onCommentClicked(ForumPost post) {
        if (post.postId != null) {
            CommentDialogFragment commentDialog = CommentDialogFragment.newInstance(post.postId);
            commentDialog.show(getParentFragmentManager(), "CommentDialog");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
