package com.jameydeorio.bionicflamingo.api;

import com.jameydeorio.bionicflamingo.models.Author;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AuthorApi {
    @GET("authors/")
    Call<List<Author>> listAuthors();

    @GET("authors/{id}/")
    Call<Author> getAuthor(@Path("id") int id);
}

