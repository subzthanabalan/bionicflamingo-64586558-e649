package com.jameydeorio.bionicflamingo.api;

import com.jameydeorio.bionicflamingo.models.QueueItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface QueueApi {
    @GET("queue/")
    Call<List<QueueItem>> getQueue();

    @POST("queue/")
    Call<QueueItem> addToQueue(@Body QueueItem queueItem);
}
