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

    public void logincheck() {

        String id = loginSharedPreference.getString("username");
        String password = loginSharedPreference.getString("password");
        if ((id != null) && (password != null)) {
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

                    //Login Check in SharedPref
                    if(response.headers().toString().contains("__RequestVerificationToken=")) {
                        loginSharedPreference.putString("verificationToken", response.headers().toString().split("__RequestVerificationToken=")[1].split(";")[0]);

                        //Check SharedPref for Refresh Tokens..
                        tv.setText("Gold 会員 チェック中..");
                        String refreshToken = loginSharedPreference.getString("refresh_token");
                        if(refreshToken != null) {
                            //Todo : Make token aquision function
                            Log.e("exploit", "WellDone! go to the next stage!");
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
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
                public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Login Error!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            });
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }
    }

    public void refresh_token() {

    }
}
