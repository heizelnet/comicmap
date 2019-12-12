package com.example.comicmap.OAuth;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import okhttp3.HttpUrl;

public class LoginViewClient extends WebViewClient {
    HttpUrl authUrl = HttpUrl.parse("https://auth1-sandbox.circle.ms/OAuth2/")
            .newBuilder()
            .addQueryParameter("response_type", "code")
            .addQueryParameter("redirect_uri", "comicmap.login://redirect")
            .addQueryParameter("client_id", "comicmapgZwp98BPmh5rj35zfnFNcZA5mxrpyCUQ")
            .addQueryParameter("state","persistant")
            .addQueryParameter("scope","user_info circle_read circle_write favorite_read favorite_write")
            .build();
    private Activity activity;
    private WebView target;

    public LoginViewClient(Activity activity, WebView target) {
        this.activity = activity;
        this.target = target;
    }

    @Override
    public void onPageFinished(WebView view, String url){
        String cookies = CookieManager.getInstance().getCookie(url);
        Log.d("exploit", "All the cookies in a string:" + cookies);
    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("javascript:")) {
            try {
                Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                Intent existPackage = activity.getPackageManager().getLaunchIntentForPackage(intent.getPackage());
                if (existPackage != null) {
                    activity.startActivity(intent);
                } else {
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                    marketIntent.setData(Uri.parse("market://details?id=" + intent.getPackage()));
                    activity.startActivity(marketIntent);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(url.startsWith("https://webcatalog.circle.ms/")) {
            Log.e("exploit", "Target arrived!");
            view.loadUrl(String.valueOf(authUrl.url()));
        }
        else {
            view.loadUrl(url);
        }
        return true;
    }
/*
    protected boolean handleNotFoundScheme(String scheme) {
        //PG사에서 호출하는 url에 package정보가 없어 ActivityNotFoundException이 난 후 market 실행이 안되는 경우
        if (PaymentScheme.ISP.equalsIgnoreCase(scheme)) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_ISP)));
            return true;
        } else if (PaymentScheme.BANKPAY.equalsIgnoreCase(scheme)) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_BANKPAY)));
            return true;
        }

        return false;
    }

 */

}
