package com.example.comicmap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class SplashActivity extends AppCompatActivity {
    private DataBaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView textView = findViewById(R.id.textView);

        SplashActivityPermissionsDispatcher.checkProcessWithPermissionCheck(this);
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void checkProcess() {
        helper = new DataBaseHelper(this);
        TextView tv = findViewById(R.id.textView_load);
        Log.e("exploit", "wait for check...");
        tv.setText(R.string.loading);
        try {
            helper.createDatabase();
        } catch (Exception e) { e.printStackTrace(); }
        tv.setText(R.string.done);
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
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }, 2000);
        }
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

        SharedPreferences preferences = this.getSharedPreferences("Loginfo", MODE_PRIVATE);
        String id = preferences.getString("username", null);
        String password = preferences.getString("password", null);
        if((id !=null) && (password !=null)) {
            LoginPostCall postCall = new LoginPostCall(id, password);
            Log.e("exploit", "ID : " + id + ", PW : " + password);
            Call call = postCall.createHttpPostMethodCall();
            call.enqueue(new Callback() {
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    SharedPreferences.Editor editor = preferences.edit();
                    String verification = response.headers().toString().split("__RequestVerificationToken=")[1].split(";")[0];
                    editor.putString("verificationToken", verification);
                    editor.apply();
                    Log.e("==exploit==", "Token : " + verification);
                }

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    @SuppressLint("HandlerLeak")
                    Handler handler = new Handler() {
                        public void handleMessage(Message msg) {
                            Toast.makeText(getApplicationContext(), "Error! Login Retry", Toast.LENGTH_SHORT).show();
                            super.handleMessage(msg);
                        }
                    };
                    handler.sendEmptyMessage(0);
                }
            });
            String token = preferences.getString("verificationToken", "");
            if(!token.equals("")) {
                check = true;
                return check;
            }
        }
        Log.e("==exploit==", "=== not Registered ID & password! ===");
        return check;
    }

}
