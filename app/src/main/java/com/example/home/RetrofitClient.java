package com.example.home;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;

    // Base URL for ThingSpeak
    private static final String BASE_URL = "https://api.thingspeak.com/";

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // Convert JSON to Java objects
                    .build();
        }
        return retrofit;
    }
}
