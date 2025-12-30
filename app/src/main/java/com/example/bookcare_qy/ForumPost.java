package com.example.bookcare_qy;

import com.google.firebase.database.Exclude;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ForumPost {

    public String postId;
    public String posterId;
    public String posterName;
    public String postType;
    public String title;
    public String message;
    public Date timestamp;
    public int commentCount;
    public Map<String, Boolean> likes = new HashMap<>(); // The correct way to store likes

    public ForumPost() {
        // Default constructor required for Firebase
    }

    public ForumPost(String posterName, String postType, String title, String message, Date timestamp, int commentCount) {
        this.posterName = posterName;
        this.postType = postType;
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
        this.commentCount = commentCount;
    }

    // --- Getters for all public fields ---
    public String getPostId() { return postId; }
    public String getPosterId() { return posterId; }
    public String getPosterName() { return posterName; }
    public String getPostType() { return postType; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public Date getTimestamp() { return timestamp; }
    public int getCommentCount() { return commentCount; }
    public Map<String, Boolean> getLikes() { return likes; }

    // --- Helper method for the like count ---
    @Exclude
    public int getLikeCount() {
        return likes.size();
    }
}
