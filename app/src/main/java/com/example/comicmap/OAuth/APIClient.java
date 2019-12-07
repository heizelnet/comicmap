package com.example.comicmap.OAuth;


import com.example.comicmap.LoginSharedPreference;
import com.example.comicmap.MyApplication;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;

public class APIClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String base_url) {
        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(MyApplication.getAppContext()));
        LoginSharedPreference loginSharedPreference = new LoginSharedPreference();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer "+loginSharedPreference.getString("access_token")).build();
            return chain.proceed(request);
        });
        retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .client(httpClient.cookieJar(cookieJar).build())
                .build();
        return retrofit;
    }
}
