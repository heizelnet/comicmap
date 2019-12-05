package com.example.comicmap.OAuth;


import com.example.comicmap.MyApplication;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class LoginClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String base_url) {
        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(MyApplication.getAppContext()));
        OkHttpClient client = new OkHttpClient.Builder().cookieJar(cookieJar).build();


        retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .client(client)
                .build();
        return retrofit;
    }
}
