package com.ensiie.yuheng.moviie.rest;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by solael on 2017/1/21.
 */

public class ApiClient {

    private static final String BASE_URL = "http://api.themoviedb.org/3/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            // setup interceptor to see more detailed log
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // define okhttpclient connection to combine with retrofit2
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(httpLoggingInterceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
