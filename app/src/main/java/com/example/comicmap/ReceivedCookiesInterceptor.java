package com.example.comicmap;


import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;


public class ReceivedCookiesInterceptor implements Interceptor {
    private LoginSharedPreference mDsp_;

    public ReceivedCookiesInterceptor(){
        mDsp_ = new LoginSharedPreference();
    }

    @NotNull
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        String cookie_pattern1 = ".AspNet.ExternalCookie=";
        String cookie_pattern2 = ".ASPXAUTH=";
        String cookie_pattern3 = "__RequestVerificationToken=";
        String cookie_pattern4 = ".AspNet.ApplicationCookie=";
        String cookie_pattern5 = "ARRAffinity=";
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = mDsp_.getHashSet(LoginSharedPreference.KEY_COOKIE, new HashSet<>());
            for (String header : originalResponse.headers("Set-Cookie")) {
                if(header.contains(cookie_pattern1) && (!cookies.contains(cookie_pattern1))) {
                    cookies.add(header);
                    Log.e("exploit", "pattern1");
                }
                if(header.contains(cookie_pattern2) && (!cookies.contains(cookie_pattern2))) {
                    cookies.add(header);
                    Log.e("exploit", "pattern2");
                }
                if(header.contains(cookie_pattern3) && (!cookies.contains(cookie_pattern3))) {
                    cookies.add(header);
                    Log.e("exploit", "pattern3");
                }
                if(header.contains(cookie_pattern4) && (!cookies.contains(cookie_pattern4))) {
                    cookies.add(header);
                    Log.e("exploit", "pattern4");
                }
                if(header.contains(cookie_pattern5) && (!cookies.contains(cookie_pattern5))) {
                    cookies.add(header);
                    Log.e("exploit", "pattern5");
                }

            }
            mDsp_.putHashSet(LoginSharedPreference.KEY_COOKIE, cookies);
        }
        return originalResponse;
    }

}

