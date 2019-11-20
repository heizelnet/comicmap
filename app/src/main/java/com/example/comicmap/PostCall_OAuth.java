package com.example.comicmap;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class PostCall_OAuth {
    private String url;
    private OkHttpClient okHttpClient;

    public PostCall_OAuth() {
        url = "https://auth1-sandbox.circle.ms/OAuth2";
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
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept-Language", "ko,en-US;q=0.9,en;q=0.8,ja;q=0.7")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .post(formBody)
                .build();

        // Create a new Call object with post method.
        Call call = okHttpClient.newCall(request);

        return call;
    }
}
