package com.hazelnut.comicmap.Activity;

import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;


import com.hazelnut.comicmap.Logger;
import com.hazelnut.comicmap.OAuth.APIClient;
import com.hazelnut.comicmap.OAuth.LoginViewClient;
import com.hazelnut.comicmap.OAuth.TokenProcess;
import com.hazelnut.comicmap.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginUrlActivity extends AppCompatActivity {

    private WebView webView;
    private retrofit2.Call<ResponseBody> responseBodyCall;
    private TokenProcess apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginurl);

        webView = findViewById(R.id.webViewclient);
        webView.setWebViewClient(new LoginViewClient(this, webView));
        WebSettings settings = webView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        apiInterface = APIClient.getClient(TokenProcess.BASE_URL).create(TokenProcess.class);
        responseBodyCall = apiInterface.oauth("code", "comicmap.login://redirect",
                getResources().getString(R.string.client_id), "persistant",
                "user_info circle_read circle_write favorite_read favorite_write");
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Logger.e("exploit", "message : " + response.raw().request().url());
                    CookieManager cookieManager = CookieManager.getInstance();
                    cookieManager.setAcceptCookie(true);
                    cookieManager.setAcceptThirdPartyCookies(webView, true);

                    webView.loadUrl(String.valueOf(response.raw().request().url()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                finish();
            }
        });

    }
}
