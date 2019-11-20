package com.example.comicmap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.HashSet;

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
import retrofit2.Retrofit;

@RuntimePermissions
public class SplashActivity extends AppCompatActivity {
    private DataBaseHelper helper;
    private LoginSharedPreference loginSharedPreference = new LoginSharedPreference();

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
        TextView tv = findViewById(R.id.textView_load);
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
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }, 5000);


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
            PostCall_Login postCall = new PostCall_Login(id, password);
            Log.e("exploit", "================Login Process...================");
            Call call = postCall.createHttpPostMethodCall();
            call.enqueue(new Callback() {
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String verification = response.headers().toString().split("__RequestVerificationToken=")[1].split(";")[0];
                    loginSharedPreference.putString("verificationToken", verification);
                }

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
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
        TokenProcess apiInterface = APIClient.getClient(TokenProcess.BASE_URL).create(TokenProcess.class);
        Log.e("exploit", "================Permission Process...================");
        retrofit2.Call<ResponseBody> responseBodyCall = apiInterface.addParameters(
                "code",
                "comicmapgZwp98BPmh5rj35zfnFNcZA5mxrpyCUQ",
                "test",
                "user_info circle_read circle_write favorite_read favorite_write");

        responseBodyCall.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String result = null;
                try {
                    result = response.body().string();
                    Log.e("exploit", "Result : " + result);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });

        return false;
    }

}
