package com.jameydeorio.bionicflamingo.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

public class ServiceGenerator {
    private static final String API_BASE_URL = "http://104.236.18.46:9090/api/";
//    private static final String API_BASE_URL = "http://private-823ee-safari.apiary-mock.com";

    private static Retrofit.Builder mRetrofitBuilder = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    public static <T> T createService(Class<T> serviceClass) {
        HttpLoggingInterceptor mLoggingInterceptor = new HttpLoggingInterceptor();
        mLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClientBuilder = new OkHttpClient.Builder().addInterceptor(mLoggingInterceptor).build();
        Retrofit retrofit = mRetrofitBuilder.client(httpClientBuilder).build();
        return retrofit.create(serviceClass);
    }
}
