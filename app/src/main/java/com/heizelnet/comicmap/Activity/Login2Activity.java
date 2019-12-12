package com.heizelnet.comicmap.Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.webkit.CookieManager;
import android.widget.TextView;

import com.heizelnet.comicmap.DataBaseHelper;
import com.heizelnet.comicmap.LoginSharedPreference;
import com.heizelnet.comicmap.OAuth.APIClient;
import com.heizelnet.comicmap.OAuth.LoginClient;
import com.heizelnet.comicmap.OAuth.TokenProcess;
import com.heizelnet.comicmap.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login2Activity extends AppCompatActivity {

    private String code, state, error_code, error;
    private LoginSharedPreference loginSharedPreference = new LoginSharedPreference();
    private TokenProcess tokenInterface;
    private retrofit2.Call<ResponseBody> responseBodyCall;
    private TokenProcess apiInterface;
    private CookieManager cookieManager;
    private DataBaseHelper helper;
    private SQLiteDatabase mDataBase;
    private TextView tv_load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        tv_load = (TextView)findViewById(R.id.textView_load_login);


        String REDIRECT_URI_ROOT="comicmap.login://redirect";
        String CODE = "code";
        String STATE = "state";
        String ERROR_CODE = "error_code";
        String ERROR = "error";
        helper = new DataBaseHelper(this);
        mDataBase = helper.openDataBase();


        Uri data = getIntent().getData();
        cookieManager = CookieManager.getInstance();


        if (data != null && !TextUtils.isEmpty(data.getScheme())) {
            code = data.getQueryParameter(CODE);
            state = data.getQueryParameter(STATE);
            error_code = data.getQueryParameter(ERROR_CODE);
            error = data.getQueryParameter(ERROR);
            if (!TextUtils.isEmpty(code)) {
                //Login Success & Gold User
                ////Log.e("exploit", "code : " + code + ", state : " + state);
                loginSharedPreference.putInt("IS_VERIFICATED", 0);
                accessToken();
            }
            if(!TextUtils.isEmpty(error)) {
                //Not Gold User.. or Not Permmited...
                ////Log.e("exploit", "onCreate: handle result of authorization with error :" + error);
                cookieManager.removeAllCookies(null);
                //then die
                finish();
            } if(!TextUtils.isEmpty(error_code)) {
                //a problem occurs, the user reject our granting request or something like that
                ////Log.e("exploit", "onCreate: handle result of authorization with error :" + error_code);
                //then die
                cookieManager.removeAllCookies(null);
                finish();
            }
        }
    }

    //After get Code, access API & get Token
    public void accessToken() {
        Log.e("exploit", "access token");
        tv_load.setText("Access Token...");
        HashMap<String, Object> postData2 = new HashMap<>();
        postData2.put("grant_type", "authorization_code");
        postData2.put("code", code);
        postData2.put("client_id", getResources().getString(R.string.client_id));
        postData2.put("client_secret", getResources().getString(R.string.client_secret));
        tokenInterface = LoginClient.getClient(TokenProcess.BASE_URL).create(TokenProcess.class);
        responseBodyCall = tokenInterface.accessToken(postData2);
        responseBodyCall.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull retrofit2.Call<ResponseBody> call, @NotNull retrofit2.Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    ////Log.e("exploit", "Token retrieved! : " + result);

                    JSONObject json = new JSONObject(result);
                    String access_token = json.getString("access_token");
                    String refresh_token = json.getString("refresh_token");
                    loginSharedPreference.putString("access_token", access_token);
                    loginSharedPreference.putString("refresh_token", refresh_token);

                    //Set TimeStamp
                    int lastTime = (int) SystemClock.elapsedRealtime();
                    loginSharedPreference.putInt("elpasedTime", lastTime/1000 );

                    ////Log.e("exploit", "Token retrieved! : " + access_token + ", " + refresh_token);
                    show_favorite();
                } catch (Exception e) {
                    ////Log.e("exploit", "Access_token Error!, maybe requestverification_token");
                    cookieManager.removeAllCookies(null);
                    finish();
                }

            }
            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                tv_load.setText("Network Error! Retry..");
                ////Log.e("exploit", "onFailure");
                accessToken();
            }
        });
    }

    //After Access Token & First update favorite data
    public void show_favorite() {
        tv_load.setText("User Data Update..");
        apiInterface = APIClient.getClient(TokenProcess.API_URL).create(TokenProcess.class);
        responseBodyCall = apiInterface.getFavoriteList(getResources().getString(R.string.event_id), getResources().getInteger(R.integer.event_no), 0);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                try {
                    String jString = response.body().string();
                    JSONObject res1 = new JSONObject(jString).getJSONObject("response");
                    if(res1.getInt("maxcount") == 0) {
                        startActivity(new Intent(Login2Activity.this, MainActivity.class));
                        finish();
                    } else {
                        //Add Items
                        JSONArray list = res1.getJSONArray("list");
                        for(int i=0; i<list.length(); i++) {
                            JSONObject tmp = list.getJSONObject(i).getJSONObject("favorite");
                            int wid = tmp.getInt("wcid");
                            int favorite_color = tmp.getInt("color");

                            String query = String.format(Locale.KOREA, "update circle_info set favorite=%d where wid=%d", favorite_color, wid);
                            mDataBase.execSQL(query);
                        }
                        ////Log.e("exploit", "WellDone!");
                        loginSharedPreference.putInt("IS_VERIFICATED", 1);
                        startActivity(new Intent(Login2Activity.this, MainActivity.class));
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ////Log.e("exploit", "favorite_exception..");
                    tv_load.setText("Error with favorite.. Retry!");
                    show_favorite();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ////Log.e("exploit", "Favorite failed...");
                tv_load.setText("Error with favorite.. Retry!");
                show_favorite();
            }
        });
    }
}

/*
        List<Cookie> cookies = new SharedPrefsCookiePersistor(MyApplication.getAppContext()).loadAll();
        for (Cookie cookie : cookies) {
            ////Log.e("exploit", "cookieValue : " + cookie.toString());
            //bundle.putString("Cookie", cookie.toString());
            //CookieManager.getInstance().setCookie(TargetUrl, cookie.toString());
        }
 */