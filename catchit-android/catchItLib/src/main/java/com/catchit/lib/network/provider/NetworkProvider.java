package com.catchit.lib.network.provider;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkProvider {

    private final Retrofit mRetrofit;

    private NetworkProvider(String serverAddress)
    {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(String.format("%s:9000", serverAddress))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getHttpClient() {
        return mRetrofit;
    }

    public static class Builder {
        String mServerAddress;

        public NetworkProvider.Builder setServerAddress(String serverAddress) {
            mServerAddress = serverAddress;
            return this;
        }

        public NetworkProvider build() {

            if(mServerAddress == null) {
                throw new IllegalArgumentException("You must provide Server Address in NetworkProvider");
            }

            return new NetworkProvider(mServerAddress);
        }
    }
}
