package com.jameydeorio.bionicflamingo.api;

import com.jameydeorio.bionicflamingo.models.Book;
import com.jameydeorio.bionicflamingo.models.QueueItem;

import java.util.List;
import java.util.Queue;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface QueueApi {
    @GET("queue/")
    Call<List<QueueItem>> getQueue();

    @GET("queue/{id}/")
    Call<QueueItem> getQueueItem(@Path("id") int id);

    @POST("queue/")
    Call<QueueItem> addToQueue(@Body QueueItem queueItem);

    @DELETE("queue/{id}/")
    Call<QueueItem> deleteFromQueue(@Path("id") int id);
}
