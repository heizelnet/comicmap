package com.heizelnet.comicmap.OAuth;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.heizelnet.comicmap.LoginSharedPreference;
import com.heizelnet.comicmap.R;

import okhttp3.HttpUrl;

public class LoginViewClient extends WebViewClient {
    HttpUrl authUrl;
    private Activity activity;
    private WebView target;
    private LoginSharedPreference loginSharedPreference = new LoginSharedPreference();

    public LoginViewClient(Activity activity, WebView target) {
        this.activity = activity;
        this.target = target;
        authUrl = HttpUrl.parse("https://auth1.circle.ms/OAuth2/")
                .newBuilder()
                .addQueryParameter("response_type", "code")
                .addQueryParameter("redirect_uri", "comicmap.login://redirect")
                .addQueryParameter("client_id", activity.getResources().getString(R.string.client_id))
                .addQueryParameter("state", "persistant")
                .addQueryParameter("scope", "user_info circle_read circle_write favorite_read favorite_write")
                .build();
    }

    @Override
    public void onPageFinished(WebView view, String url) {
            CookieManager cookieManager = CookieManager.getInstance();
            String cookie = cookieManager.getCookie(url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        //Get Intent Scheme Part
        if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("javascript:")) {
            try {
                Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                Intent existPackage = activity.getPackageManager().getLaunchIntentForPackage(intent.getPackage());

                //Cookie restore

                if (existPackage != null) {
                    activity.startActivity(intent);
                    activity.finish();
                } else {
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                    marketIntent.setData(Uri.parse("market://details?id=" + intent.getPackage()));
                    activity.startActivity(marketIntent);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

        //Redirect for Twitter, Pixiv Login..
        } else if (url.startsWith("https://webcatalog.circle.ms/") && !url.endsWith("/Account/Login")) {
            //Log.e("exploit", "Target arrived!");
            String cookies = CookieManager.getInstance().getCookie(url);
            loginSharedPreference.putString("TMP_COOKIE", cookies);
            view.loadUrl(String.valueOf(authUrl.url()));
        } else {
            view.loadUrl(url);
        }
        return true;
    }
}
