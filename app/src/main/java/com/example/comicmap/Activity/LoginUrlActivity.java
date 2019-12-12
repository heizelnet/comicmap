package com.example.comicmap.Activity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comicmap.MyApplication;
import com.example.comicmap.OAuth.LoginViewClient;
import com.example.comicmap.R;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

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
        settings.setJavaScriptEnabled(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        HttpUrl authUrl = HttpUrl.parse("https://auth1-sandbox.circle.ms/OAuth2/")
                .newBuilder()
                .addQueryParameter("response_type", "code")
                .addQueryParameter("redirect_uri", "comicmap.login://redirect")
                .addQueryParameter("client_id", "comicmapgZwp98BPmh5rj35zfnFNcZA5mxrpyCUQ")
                .addQueryParameter("state","persistant")
                .addQueryParameter("scope","user_info circle_read circle_write favorite_read favorite_write")
                .build();

        String TargetUrl = getIntent().getStringExtra("targetUrl");
        if(TargetUrl == null)
            TargetUrl = String.valueOf(authUrl.url());
        else
            TargetUrl = TargetUrl + "https://webcatalog.circle.ms";

        Log.e("exploit", "target : " + TargetUrl);


        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(webView, true);
        Bundle bundle = new Bundle();
        List<Cookie> cookies = new SharedPrefsCookiePersistor(MyApplication.getAppContext()).loadAll();
        for (Cookie cookie : cookies) {
            Log.e("exploit", "cookieValue : " + cookie.toString());
            bundle.putString("Cookie", cookie.toString());
            CookieManager.getInstance().setCookie(TargetUrl, cookie.toString());
        }

        webView.loadUrl(TargetUrl);

    }
}
