package com.hazelnut.comicmap.Activity;

import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;


import com.hazelnut.comicmap.OAuth.LoginViewClient;
import com.hazelnut.comicmap.R;

import okhttp3.HttpUrl;


public class LoginUrlActivity extends AppCompatActivity {

    private WebView webView;

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

        HttpUrl authUrl = HttpUrl.parse(getResources().getString(R.string.auth_url))
                .newBuilder()
                .addQueryParameter("response_type", "code")
                .addQueryParameter("redirect_uri", "comicmap.login://redirect")
                .addQueryParameter("client_id", getResources().getString(R.string.client_id))
                .addQueryParameter("state", "persistant")
                .addQueryParameter("scope", "user_info circle_read circle_write favorite_read favorite_write")
                .build();

        //Log.e("exploit", "target : " + TargetUrl);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(webView, true);

        webView.loadUrl(String.valueOf(authUrl.url()));

    }
}
