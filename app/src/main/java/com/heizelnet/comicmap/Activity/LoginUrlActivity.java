package com.heizelnet.comicmap.Activity;

import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;


import com.heizelnet.comicmap.OAuth.LoginViewClient;
import com.heizelnet.comicmap.R;


public class LoginUrlActivity extends AppCompatActivity {

    private String SCHEME_TAG = "comicmap.login://redirect";
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


        String TargetUrl = getIntent().getStringExtra("targetUrl");
        if(TargetUrl == null)
            TargetUrl = "https://webcatalog.circle.ms/Account/Login";

        //Log.e("exploit", "target : " + TargetUrl);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(webView, true);

        webView.loadUrl(TargetUrl);

    }
}
