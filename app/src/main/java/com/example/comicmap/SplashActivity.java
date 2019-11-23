package com.example.comicmap;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Timer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class SplashActivity extends AppCompatActivity {
    private TextView tv;
    private DataBaseHelper helper;
    private LoginSharedPreference loginSharedPreference = new LoginSharedPreference();
    private TokenProcess tokenInterface = TokenClient.getClient(TokenProcess.BASE_URL).create(TokenProcess.class);
    private retrofit2.Call<ResponseBody> responseBodyCall;

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
        helper = new DataBaseHelper(this);
        tv = findViewById(R.id.textView_load);
        try {
            helper.createDatabase();
        } catch (Exception e) { e.printStackTrace(); }
        tv.setText(R.string.done);

        //Login With check ID, Password in SharedPref
        Handler timer = new Handler();
        if(!logincheck()) {
            timer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }, 2000);
        } else {

            timer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    permission_check();
                }
            }, 3000);


        }

        //Check Permission with Comicmarket OAuth Permissions..
        
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

    public boolean logincheck() {
        boolean check = false;

        //Clean Cookies... f**k;
        loginSharedPreference.putHashSet(LoginSharedPreference.KEY_COOKIE, new HashSet<String>());

        String id = loginSharedPreference.getString("username");
        String password = loginSharedPreference.getString("password");
        if((id !=null) && (password !=null)) {
            Log.e("exploit", "================Login Process...================");
            TokenProcess loginInterface = TokenClient.getClient(TokenProcess.LOGIN_URL).create(TokenProcess.class);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("ReturnUrl", "https://webcatalog.circle.ms/Account/Login");
            hashMap.put("state", "/");
            hashMap.put("Username", id);
            hashMap.put("Password", password);
            retrofit2.Call<ResponseBody> responseBodyCall = loginInterface.LoginData(hashMap);
            responseBodyCall.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    String verification = response.headers().toString().split("__RequestVerificationToken=")[1].split(";")[0];
                    loginSharedPreference.putString("verificationToken", verification);
                }

                @Override
                public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Network Error!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            String token = loginSharedPreference.getString("verificationToken");
            if(!token.equals("")) {
                check = true;
                return check;
            }
        }
        Log.e("==exploit==", "=== not Registered ID & password! ===");
        return check;
    }

    public boolean permission_check() {
        boolean check = false;
        tokenInterface = TokenClient.getClient(TokenProcess.BASE_URL).create(TokenProcess.class);
        Log.e("exploit", "================Permission Process...================");
        responseBodyCall = tokenInterface.addParameters(
                "code",
                "comicmapgZwp98BPmh5rj35zfnFNcZA5mxrpyCUQ",
                "test",
                "user_info circle_read circle_write favorite_read favorite_write");

        responseBodyCall.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    String req_token = result.split("<input name=\"__RequestVerificationToken\" type=\"hidden\" value=\"")[1].split("\" />")[0];
                    loginSharedPreference.putString("verificationParamToken", req_token);
                    Log.e("exploit", "Result token : " + req_token);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                });
                finish();
            }
        });
        tv.setText("Gold 会員 チェック中..");

        Handler timer = new Handler();
        timer.postDelayed(new Runnable() {
            @Override
            public void run() {
                HashMap<String, Object> postData = new HashMap<>();
                postData.put("__RequestVerificationToken", loginSharedPreference.getString("verificationParamToken"));
                postData.put("isApproved", "true");
                postData.put("client_id", "comicmapgZwp98BPmh5rj35zfnFNcZA5mxrpyCUQ");
                postData.put("redirect_uri", "https://webcatalog.circle.ms/");
                postData.put("state", "test");
                postData.put("scope", "user_info circle_read circle_write favorite_read favorite_write");
                postData.put("response_type", "code");
                responseBodyCall = tokenInterface.postData(postData);
                responseBodyCall.enqueue(new retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        try {
                            String result = response.toString();
                            Log.e("exploit", "Result : " + result);
                            loginSharedPreference.putString("tokenCode", result.split("code=")[2].split("&state")[0]);
                            accessToken();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

                    }
                });

            }
        }, 3000);

        return false;
    }

    public boolean accessToken() {
        tv.setText("いらっしゃいませ!");
        Log.e("exploit", "============= access Token...================");
        Handler timer = new Handler();
        timer.postDelayed(new Runnable() {
            @Override
            public void run() {
                HashMap<String, Object> postData2 = new HashMap<>();
                postData2.put("grant_type", "authorization_code");
                postData2.put("code", loginSharedPreference.getString("tokenCode"));
                Log.e("exploit", "tokenCode : " + loginSharedPreference.getString("tokenCode"));
                postData2.put("client_id", "comicmapgZwp98BPmh5rj35zfnFNcZA5mxrpyCUQ");
                postData2.put("client_secret", "bGLDLnC7NrwFnWR3a8C2hz9sYEJtcnLhMwRJHdwV");
                responseBodyCall = tokenInterface.accessToken(postData2);
                responseBodyCall.enqueue(new retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        try {
                            String result = response.body().string();
                            Log.e("exploit", "Token retrieved! : " + result);
                            //loginSharedPreference.putString("tokenCode", result.split("code=")[1].split("&state")[0]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Network Error!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        finish();
                    }
                });
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 3000);


        return false;
    }

}
