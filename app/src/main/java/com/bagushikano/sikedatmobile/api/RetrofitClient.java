package com.bagushikano.sikedatmobile.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    //TODO change to final endpoint
    private static final String baseURL = "https://sikramat.herokuapp.com/api/";
    private static retrofit2.Retrofit retrofit;

    //    .addInterceptor(new LoggingInterceptor.Builder().setLevel(Level.BASIC).build())
    public static retrofit2.Retrofit buildRetrofit() {
        if(retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
