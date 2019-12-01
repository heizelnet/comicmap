package com.example.comicmap;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.comicmap.Activity.LoginActivity;
import com.example.comicmap.Activity.MainActivity;
import com.example.comicmap.Activity.PermissionActivity;
import com.example.comicmap.OAuth.APIClient;
import com.example.comicmap.OAuth.TokenProcess;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

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
        tv_load = findViewById(R.id.textView_load);
        try {
            helper.createDatabase();
        } catch (Exception e) { e.printStackTrace(); }

        //SearchBox Update
        tv_load.setText(R.string.done);
        Log.e("exploit", "database updating..");
        if(dataSharedPreference.getStringArrayList(dataSharedPreference.SEARCH_BOX).isEmpty()) {
            mDataBase = helper.openDataBase();
            String query = "select Name, Author from circle_info";
            Cursor cur = mDataBase.rawQuery(query, null);
            cur.moveToFirst();

            //iterate query add items to dialog
            ArrayList<String> items = new ArrayList<>();
            Log.e("exploit", "query count : " + cur.getCount());
            String Name, Author;
            if (cur.getCount() != 0) {
                while (true) {
                    try {
                        Name = (cur.getString(cur.getColumnIndex("Name")).equals("")) ? " ": cur.getString(cur.getColumnIndex("Name"));
                        Author = (cur.getString(cur.getColumnIndex("Author")).equals("")) ? " ": cur.getString(cur.getColumnIndex("Author"));
                        items.add(Name + " / " +
                                Author);
                    } catch (Exception e) {
                        break;
                    }
                    cur.moveToNext();
                }
            }
            dataSharedPreference.setStringArrayList(dataSharedPreference.SEARCH_BOX, items);
            Log.e("exploit", "Update complete!");
        }


        //Login With check ID, Password in SharedPref
        //logincheck();

        //client_id, client_secret이 만료되어 잠시 중단...
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
        
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

        String id = loginSharedPreference.getString("username");
        String password = loginSharedPreference.getString("password");
        if ((id != null) && (password != null)) {
            Log.e("exploit", "================Login Process...================");
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("ReturnUrl", "https://webcatalog.circle.ms/Account/Login");
            hashMap.put("state", "/");
            hashMap.put("Username", id);
            hashMap.put("Password", password);
            loginInterface = APIClient.getClient(TokenProcess.LOGIN_URL).create(TokenProcess.class);
            responseBodyCall = loginInterface.LoginData(hashMap);
            responseBodyCall.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull retrofit2.Call<ResponseBody> call, @NotNull retrofit2.Response<ResponseBody> response) {

                    //Login Check in SharedPref
                    if(response.headers().toString().contains("__RequestVerificationToken=")) {
                        loginSharedPreference.putString("verificationToken", response.headers().toString().split("__RequestVerificationToken=")[1].split(";")[0]);

                        //Check SharedPref for Refresh Tokens..
                        tv_load.setText("Gold 会員 チェック中..");
                        String refreshToken = loginSharedPreference.getString("refresh_token");
                        if(refreshToken != null) {
                            //Todo : Make token aquision function
                            refresh_token();
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
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }
    }

    //Refresh Token & Go to Main page
    public void refresh_token() {
        HashMap<String, Object> postData2 = new HashMap<>();
        postData2.put("grant_type", "refresh_token");
        postData2.put("refresh_token", loginSharedPreference.getString("refresh_token"));
        postData2.put("client_id", "comicmapgZwp98BPmh5rj35zfnFNcZA5mxrpyCUQ");
        postData2.put("client_secret", "bGLDLnC7NrwFnWR3a8C2hz9sYEJtcnLhMwRJHdwV");
        loginInterface = APIClient.getClient(TokenProcess.BASE_URL).create(TokenProcess.class);
        responseBodyCall = loginInterface.accessToken(postData2);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {

                //Todo : Check GoldUser payment time has Expired...
                try {
                    String result = response.body().string();
                    JSONObject json = new JSONObject(result);
                    String access_token = json.getString("access_token");
                    String refresh_token = json.getString("refresh_token");
                    loginSharedPreference.putString("access_token", access_token);
                    loginSharedPreference.putString("refresh_token", refresh_token);
                    Log.e("exploit", "Token retrieved! : " + access_token + ", " + refresh_token);
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } catch(Exception e) {}
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                tv_load.setText("errorが発生します。 Retry..");
                refresh_token();
            }
        });
    }
}
