package com.jameydeorio.bionicflamingo.models;

import com.google.gson.annotations.SerializedName;

public class QueueItem {
    private int mId;
    @SerializedName("id") private int identifier;
    private int user;
    private int book;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getBook() {
        return book;
    }

    public void setBook(int book) {
        this.book = book;
    }

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }
}
