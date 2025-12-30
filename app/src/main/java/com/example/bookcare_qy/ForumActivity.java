package com.example.bookcare_qy;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ForumActivity extends AppCompatActivity implements PostAdapter.OnPostInteractionListener {

    private String currentUserId = "user123";
    private RecyclerView rvPosts;
    private FloatingActionButton fabNewPost;
    private PostAdapter postAdapter;
    private List<ForumPost> postsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        setupViews();
    }

    private void setupViews() {
        rvPosts = findViewById(R.id.rvPosts);
        fabNewPost = findViewById(R.id.fabNewPost);

        postsList = new ArrayList<>();
        postAdapter = new PostAdapter(postsList, this);
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        rvPosts.setAdapter(postAdapter);

        fabNewPost.setOnClickListener(v -> showCreatePostDialog());
    }

    @Override
    public void onLikeClicked(ForumPost post, int position) {
        // This functionality is now disabled
    }

    @Override
    public void onCommentClicked(ForumPost post) {
        // Reverted to a simple Toast message
        Toast.makeText(this, "Comment button clicked for: " + post.getTitle(), Toast.LENGTH_SHORT).show();
    }

    private void showCreatePostDialog() {
        CreatePostDialog dialog = new CreatePostDialog();
        dialog.show(getSupportFragmentManager(), "CreatePostDialog");
    }

    public void createNewPost(String postType, String title, String message) {
        // This functionality is now disabled
    }
}
