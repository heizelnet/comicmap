package com.example.comicmap;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class TokenPostCall {
    private String url;
    private OkHttpClient okHttpClient;

    //Todo list : Change URL, PostCall -> GetCall,
    public TokenPostCall(String id, String password) {
        url = "https://auth2.circle.ms";
        okHttpClient = new OkHttpClient();
    }

    /* Create OkHttp3 Call object use post method with url. */
    public Call createHttpPostMethodCall() {
        // Create okhttp3 form body builder.
        FormBody.Builder formBodyBuilder = new FormBody.Builder();

        // Add form parameter
        formBodyBuilder.add("ReturnUrl", "https://webcatalog.circle.ms/Account/Login");
        formBodyBuilder.add("state", "/");

        // Build form body.
        FormBody formBody = formBodyBuilder.build();

        // Create a http request object.
        Request.Builder builder = new Request.Builder();
        builder = builder.url(url);
        builder = builder.addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept-Language", "ko,en-US;q=0.9,en;q=0.8,ja;q=0.7")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36")
                .addHeader("Accept-Encoding", "gzip, deflate, br");
        builder = builder.post(formBody);
        Request request = builder.build();

        // Create a new Call object with post method.
        Call call = okHttpClient.newCall(request);

        return call;
    }
}
