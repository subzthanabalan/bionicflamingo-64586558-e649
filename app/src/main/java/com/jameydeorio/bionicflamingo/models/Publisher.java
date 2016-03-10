package com.jameydeorio.bionicflamingo.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Subathra Thanabalan on 3/10/16.
 */
public class Publisher {
    private int mId;
    @SerializedName("id") private int identifier;
    private String name;

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }
}
