package com.hazelnut.comicmap.OAuth;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hazelnut.comicmap.LoginSharedPreference;

public class LoginViewClient extends WebViewClient {
    private Activity activity;
    private WebView target;
    private LoginSharedPreference loginSharedPreference = new LoginSharedPreference();

    public LoginViewClient(Activity activity, WebView target) {
        this.activity = activity;
        this.target = target;
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
        } else {
            String cookies = CookieManager.getInstance().getCookie(url);
            loginSharedPreference.putString("TMP_COOKIE", cookies);
            view.loadUrl(url);
        }
        return true;
    }
}
