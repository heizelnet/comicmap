package com.example.comicmap.OAuth;


import android.util.Log;
import android.webkit.CookieManager;

import com.example.comicmap.MyApplication;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;

public class LoginClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String base_url) {
        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(MyApplication.getAppContext()));
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());

                if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                    HashSet<String> cookies = new HashSet<>();
                    for (String header : originalResponse.headers("Set-Cookie")) {
                        cookies.add(header);
                    }
                    // Save the cookies (I saved in SharedPrefrence), you save whereever you want to save
                    Log.e("exploit", "cookies : " + cookies);
                }
                return originalResponse;
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .client(httpClient.cookieJar(cookieJar).connectTimeout(10, TimeUnit.SECONDS).build())
                .build();
        return retrofit;
    }
}
