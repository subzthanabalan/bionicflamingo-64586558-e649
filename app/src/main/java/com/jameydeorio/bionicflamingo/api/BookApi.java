package com.jameydeorio.bionicflamingo.api;

import com.jameydeorio.bionicflamingo.models.Book;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BookApi {
    @GET("books/")
    Call<List<Book>> listBooks();

    @GET("books/{id}/")
    Call<Book> getBook(@Path("id") int id);
}
