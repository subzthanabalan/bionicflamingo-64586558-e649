package com.jameydeorio.bionicflamingo.api;

import com.jameydeorio.bionicflamingo.models.Publisher;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Subathra Thanabalan on 3/10/16.
 */
public interface PublisherApi {
    @GET("publishers/")
    Call<List<Publisher>> listPublishers();

    @GET("publishers/{id}/")
    Call<Publisher> getPublisher(@Path("id") int id);
}
