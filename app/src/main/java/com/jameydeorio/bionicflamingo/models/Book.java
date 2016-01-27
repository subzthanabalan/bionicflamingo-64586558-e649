package com.jameydeorio.bionicflamingo.models;

import com.google.gson.annotations.SerializedName;

public class Book {
    public static final String BOOK_ID_KEY = "book_id";

    private int mId;
    @SerializedName("id") private int identifier;
    private String title;
    private String isbn;
    @SerializedName("cover") private String coverUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }
}
