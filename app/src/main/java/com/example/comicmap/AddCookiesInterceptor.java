package com.example.comicmap;

import android.util.Log;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddCookiesInterceptor implements Interceptor {

    private LoginSharedPreference mDsp;
    public AddCookiesInterceptor(){
        mDsp = new LoginSharedPreference();
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();


        HashSet<String> preferences = mDsp.getHashSet(LoginSharedPreference.KEY_COOKIE, new HashSet<String>());
        for (String cookie : preferences) {
            builder.addHeader("Cookie", cookie);
            Log.e("exploit", cookie);
        }
        builder.removeHeader("User-Agent").addHeader("User-Agent", "Android");

        return chain.proceed(builder.build());
    }

}
