package com.hazelnut.comicmap.Deprecated;

import com.hazelnut.comicmap.LoginSharedPreference;

import java.io.IOException;

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
        builder.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3").
                addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36").
                addHeader("Accept-Encoding", "gzip, deflate, br");

        /*
        HashSet<String> preferences = mDsp.getHashSet(LoginSharedPreference.KEY_COOKIE, new HashSet<String>());
        for (String cookie : preferences) {
            builder.addHeader("Cookie", cookie);
            //Log.e("exploit", cookie);
        }
         */

        return chain.proceed(builder.build());
    }

}
