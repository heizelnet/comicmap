package com.example.comicmap.OAuth;

import android.util.Log;

import com.example.comicmap.LoginSharedPreference;
import com.example.comicmap.MyApplication;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

public class APIClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String base_url) {
        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(MyApplication.getAppContext()));
        LoginSharedPreference loginSharedPreference = new LoginSharedPreference();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer "+loginSharedPreference.getString("access_token")).build();
            Log.e("exploit", "access_token : "+loginSharedPreference.getString("access_token"));
            return chain.proceed(request);
        });
        retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .client(httpClient.cookieJar(cookieJar).build())
                .build();
        return retrofit;
    }
}
