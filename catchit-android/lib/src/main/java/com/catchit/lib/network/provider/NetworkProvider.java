package com.catchit.lib.network.provider;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkProvider {

    private final static String BASE_URL = "http://localhost:8080";

    private final Retrofit mRetrofit;

    private NetworkProvider()
    {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getHttpClient() {
        return mRetrofit;
    }

    public static class Builder {
        public NetworkProvider build() {
            return new NetworkProvider();
        }
    }
}
