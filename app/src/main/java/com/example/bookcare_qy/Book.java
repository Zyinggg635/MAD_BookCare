package com.example.bookcare_qy; // Make sure this matches your package name

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    public String id;
    public String title;
    public String author;
    public String condition;
    public String genre;

    // Default constructor required for calls to DataSnapshot.getValue(Book.class)
    public Book() {
    }

    public Book(String id, String title, String author, String condition, String genre) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.condition = condition;
        this.genre = genre;
    }

    protected Book(Parcel in) {
        id = in.readString();
        title = in.readString();
        author = in.readString();
        condition = in.readString();
        genre = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(condition);
        dest.writeString(genre);
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
