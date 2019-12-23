package com.hazelnut.comicmap.Deprecated;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hazelnut.comicmap.Activity.LoginUrlActivity;
import com.hazelnut.comicmap.LoginSharedPreference;
import com.hazelnut.comicmap.OAuth.PostCall_Login;
import com.hazelnut.comicmap.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private Button button;
    ImageButton twitter_button, pixiv_button;
    EditText idview, pwview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        button = findViewById(R.id.login_button);
        twitter_button = findViewById(R.id.Login_twitter);
        pixiv_button = findViewById(R.id.Login_pixiv);
        idview = findViewById(R.id.idView);
        pwview = findViewById(R.id.pwView);
        LoginSharedPreference loginSharedPreference = new LoginSharedPreference();
        button.setOnClickListener(v -> {
            String s1 = String.valueOf(idview.getText());
            String s2 = String.valueOf(pwview.getText());

            PostCall_Login postCall = new PostCall_Login(s1, s2);
            Call call = postCall.createHttpPostMethodCall();

            call.enqueue(new Callback() {
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String header = response.headers().toString();
                    if(header.contains("__RequestVerificationToken=")){
                        /*
                        String verification = response.headers().toString().split("__RequestVerificationToken=")[1].split(";")[0];
                        Log.e("==exploit==", "Token : "+verification);
                        loginSharedPreference.putString("username", s1);
                        loginSharedPreference.putString("password", s2);
                        loginSharedPreference.putString("verificationToken", verification);
                        startActivity(new Intent(LoginActivity.this, PermissionActivity.class));
                        finish();

                         */
                        Intent i = new Intent(LoginActivity.this, LoginUrlActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else {
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Login Error!", Toast.LENGTH_SHORT).show());
                    }
                }

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Toast.makeText(getApplicationContext(), "Network Error!", Toast.LENGTH_SHORT).show();
                }
            });
        });
        twitter_button.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this, LoginUrlActivity.class);
            String targetUrl = "https://auth2.circle.ms/Account/ExternalLogIn?provider=Twitter&returnUrl=";
            i.putExtra("targetUrl", targetUrl);
            Log.e("exploit", "url : " + targetUrl);
            startActivity(i);
            finish();
        });

        pixiv_button.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this, LoginUrlActivity.class);
            String targetUrl = "https://auth2.circle.ms/Account/ExternalLogIn?provider=pixiv&returnUrl=";
            i.putExtra("targetUrl", targetUrl);
            Log.e("exploit", "url : " + targetUrl);
            startActivity(i);
            finish();
        });
    }
}
