package com.example.bookcare_qy; // Make sure this matches your package name

import java.io.Serializable;

public class Book implements Serializable {
    public String id;
    public String title;
    public String author;
    public String condition;
    public String type;

    // Default constructor required for calls to DataSnapshot.getValue(Book.class)
    public Book() {
    }

    public Book(String id, String title, String author, String condition, String type) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.condition = condition;
        this.type = type;
    }

    // --- Getters and Setters for all fields ---
    // Firebase requires public getters and setters for fields it maps to objects.

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
