package com.example.bookcare_qy;

public class Book {
    private String title;
    private String author;
    private String status;
    private int views;
    private int interested;
    private String uploadedBy;

    public Book() { } // required for Firebase

    public Book(String title, String author, String status, int views, int interested) {
        this(title, author, status, views, interested, "");
    }

    public Book(String title, String author, String status, int views, int interested, String uploadedBy) {
        this.title = title;
        this.author = author;
        this.status = status;
        this.views = views;
        this.interested = interested;
        this.uploadedBy = uploadedBy;
    }

    // getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }

    public int getInterested() { return interested; }
    public void setInterested(int interested) { this.interested = interested; }

    public String getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(String uploadedBy) { this.uploadedBy = uploadedBy; }
}