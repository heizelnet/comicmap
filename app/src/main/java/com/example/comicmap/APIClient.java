package com.example.comicmap;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

class APIClient {

    private static Retrofit retrofit = null;

    static Retrofit getClient(String base_url) {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new AddCookiesInterceptor()).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .client(client)
                .build();
        return retrofit;
    }
}
