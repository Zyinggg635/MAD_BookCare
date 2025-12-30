package com.example.bookcare_qy;

import java.util.Date;

public class Comment {
    private String commenterName;
    private String text;
    private Date timestamp;

    // Default constructor is required for calls to DataSnapshot.getValue(Comment.class)
    public Comment() {
    }

    public Comment(String commenterName, String text, Date timestamp) {
        this.commenterName = commenterName;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getCommenterName() {
        return commenterName;
    }

    public String getText() {
        return text;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
