package com.hazelnut.comicmap;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.webkit.CookieManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.hazelnut.comicmap.Activity.LoginUrlActivity;
import com.hazelnut.comicmap.Activity.MainActivity;
import com.hazelnut.comicmap.OAuth.APIClient;
import com.hazelnut.comicmap.OAuth.LoginClient;
import com.hazelnut.comicmap.OAuth.TokenProcess;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.ResponseBody;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RuntimePermissions
public class SplashActivity extends AppCompatActivity {
    private TextView tv_load;
    private DataBaseHelper helper;
    private LoginSharedPreference loginSharedPreference = new LoginSharedPreference();
    private DataSharedPreference dataSharedPreference = new DataSharedPreference();
    private SQLiteDatabase mDataBase;
    private TokenProcess loginInterface;
    private retrofit2.Call<ResponseBody> responseBodyCall;
    private TokenProcess apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView textView = findViewById(R.id.textView);

        SplashActivityPermissionsDispatcher.checkProcessWithPermissionCheck(this);
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void checkProcess() {
        //Check DataBase Permission & Storage

        try {
            helper = new DataBaseHelper(this);
            tv_load = findViewById(R.id.textView_load);
            mDataBase = helper.openDataBase();
        } catch (Exception e) { e.printStackTrace(); }

        //SearchBox Update
        tv_load.setText(R.string.done);
        Logger.e("exploit", "database updating..");
        if(dataSharedPreference.getStringArrayList(dataSharedPreference.SEARCH_BOX).isEmpty()) {
            String query = "select Name, Author from circle_info";
            Cursor cur = mDataBase.rawQuery(query, null);
            cur.moveToFirst();

            //iterate query add items to dialog
            ArrayList<String> items = new ArrayList<>();
            Logger.e("exploit", "query count : " + cur.getCount());
            if (cur.getCount() != 0) {
                while (true) {
                    try {
                        if(!cur.getString(cur.getColumnIndex("Name")).equals("")) {
                            items.add("Name : " + cur.getString(cur.getColumnIndex("Name")));
                        }
                        if(!cur.getString(cur.getColumnIndex("Author")).equals("")) {
                            items.add("Author : " + cur.getString(cur.getColumnIndex("Author")));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                    cur.moveToNext();
                }
            }
            dataSharedPreference.setStringArrayList(dataSharedPreference.SEARCH_BOX, items);
            Logger.e("exploit", "Update complete!");
        }

        //Login With check ID, Password in SharedPref
        logincheck();
        
    }

    @OnShowRationale({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showDialogforCheck(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("Please Check Permission for Use.")
                .setPositiveButton(android.R.string.ok, (dialog, button) -> request.proceed())
                .setNegativeButton(android.R.string.cancel, (dialog, button) -> request.cancel())
                .setCancelable(false)
                .show();
    }

    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showDeniedForPermission() {
        Toast.makeText(this, "Please Check Permission.", Toast.LENGTH_SHORT).show();
        finish();
    }

    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showNeverAskForPermission() {
        Toast.makeText(this, "Please Check Permission.", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    //Check IF ID & PW stored in SharedPref
    public void logincheck() {

        int is_login = loginSharedPreference.getInt("IS_VERIFICATED");

        if (is_login == 1) {
            //Check SharedPref for Refresh Tokens..
            String refreshToken = loginSharedPreference.getString("refresh_token");
            if(refreshToken != null) {

                int startTime = (int) SystemClock.elapsedRealtime() / 1000;
                int lastTime = loginSharedPreference.getInt("elpasedTime");
                if((startTime - lastTime) > 85000) {
                    refresh_token();
                } else {
                    show_favorite();
                }

            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashActivity.this, LoginUrlActivity.class));
                        finish();
                    }
                }, 500);

            }
            /*
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("ReturnUrl", "https://webcatalog.circle.ms/Account/Login");
            hashMap.put("state", "/");
            hashMap.put("Username", id);
            hashMap.put("Password", password);
            loginInterface = LoginClient.getClient(TokenProcess.LOGIN_URL).create(TokenProcess.class);
            responseBodyCall = loginInterface.LoginData(hashMap);
            responseBodyCall.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull retrofit2.Call<ResponseBody> call, @NotNull retrofit2.Response<ResponseBody> response) {

                    //Login Check in SharedPref
                    if(response.headers().toString().contains("__RequestVerificationToken=")) {
                        loginSharedPreference.putString("verificationToken", response.headers().toString().split("__RequestVerificationToken=")[1].split(";")[0]);
                        Logger.e("exploit", "================Login OK...================");
                        //Check SharedPref for Refresh Tokens..
                        String refreshToken = loginSharedPreference.getString("refresh_token");
                        if(refreshToken != null) {

                            int startTime = (int) SystemClock.elapsedRealtime() / 1000;
                            int lastTime = loginSharedPreference.getInt("elpasedTime");
                            if((startTime - lastTime) > 85000) {
                                refresh_token();
                            } else {
                                show_favorite();
                            }

                        } else {
                            startActivity(new Intent(SplashActivity.this, PermissionActivity.class));
                            finish();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Login Error!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                }

                @Override
                public void onFailure(@NotNull retrofit2.Call<ResponseBody> call, @NotNull Throwable t) {
                    Toast.makeText(getApplicationContext(), "Login Error!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            });
             */
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, LoginUrlActivity.class).putExtra("targetUrl", "https://webcatalog.circle.ms/Account/Login"));
                    finish();
                }
            }, 500);
        }
    }

    //Refresh Token & Go to Main page
    public void refresh_token() {
        HashMap<String, Object> postData2 = new HashMap<>();
        postData2.put("grant_type", "refresh_token");
        postData2.put("refresh_token", loginSharedPreference.getString("refresh_token"));
        postData2.put("client_id", getResources().getString(R.string.client_id));
        postData2.put("client_secret", getResources().getString(R.string.client_secret));
        tv_load.setText("Gold 会員 チェック中..");
        Logger.e("exploit", "Refreshing token...");
        loginInterface = LoginClient.getClient(TokenProcess.BASE_URL).create(TokenProcess.class);
        responseBodyCall = loginInterface.accessToken(postData2);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                //Todo : Check GoldUser payment time has Expired...
                try {
                    String result = response.body().string();
                    Logger.e("exploit", "Result code : " + response.code());
                    Logger.e("exploit", "token_type: " +result);
                    JSONObject json = new JSONObject(result);
                    String access_token = json.getString("access_token");
                    String refresh_token = json.getString("refresh_token");
                    loginSharedPreference.putString("access_token", access_token);
                    loginSharedPreference.putString("refresh_token", refresh_token);
                    Logger.e("exploit", "Token retrieved! : " + access_token + ", " + refresh_token);

                    int lastTime = (int) SystemClock.elapsedRealtime();
                    loginSharedPreference.putInt("elpasedTime", lastTime/1000);
                    show_favorite();

                } catch(Exception e) {
                    Toast.makeText(MyApplication.getAppContext(), "Error! Access token Retry!", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            CookieManager cookieManager = CookieManager.getInstance();
                            cookieManager.removeAllCookies(null);
                            loginSharedPreference.putInt("IS_VERIFICATED", 0);
                            startActivity(new Intent(SplashActivity.this, LoginUrlActivity.class).putExtra("targetUrl", "https://webcatalog.circle.ms/Account/Login"));
                            finish();
                        }
                    }, 500);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                tv_load.setText("errorが発生します。 Retry..");
                refresh_token();
            }
        });
    }

    public void show_favorite() {
        tv_load.setText("User Data Update..");
        int lastTime = (int) SystemClock.elapsedRealtime();
        apiInterface = APIClient.getClient(TokenProcess.API_URL).create(TokenProcess.class);
        responseBodyCall = apiInterface.getFavoriteList(getResources().getString(R.string.event_id), getResources().getInteger(R.integer.event_no), 0);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                try {
                    int lastTime2 = (int) SystemClock.elapsedRealtime();
                    Log.e("exploit", "ElpasedTime : " + (lastTime2 - lastTime) / 1000);
                    String jString = response.body().string();
                    JSONObject res1 = new JSONObject(jString).getJSONObject("response");
                    if(res1.getInt("maxcount") == 0) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    } else {
                        //Add Items
                        JSONArray list = res1.getJSONArray("list");
                        for(int i=0; i<list.length(); i++) {
                            JSONObject tmp = list.getJSONObject(i).getJSONObject("favorite");
                            int wid = tmp.getInt("wcid");
                            int favorite_color = tmp.getInt("color");
                            String memo = tmp.getString("memo");
                            if(memo.isEmpty())
                                memo = "edit memo here";
                            else {
                                memo = memo.replace("\"", "");
                                memo = memo.replace("\'", "");
                            }

                            String query = String.format(Locale.KOREA, "update circle_info set favorite=%d, memo='%s' where wid=%d", favorite_color, memo, wid);
                            Logger.e("exploit", "query : " + query);
                            mDataBase.execSQL(query);
                        }
                        Logger.e("exploit", "WellDone!");
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    tv_load.setText("Error with favorite.. Retry!");
                    show_favorite();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else if(t instanceof IOException) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }else {
                    tv_load.setText("Error with favorite.. Retry!");
                    show_favorite();
                }
            }
        });
    }
}
