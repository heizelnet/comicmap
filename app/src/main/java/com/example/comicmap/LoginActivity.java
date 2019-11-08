package com.example.comicmap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private Button button;
    EditText idview, pwview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        button = findViewById(R.id.login_button);
        idview = findViewById(R.id.idView);
        pwview = findViewById(R.id.pwView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = String.valueOf(idview.getText());
                String s2 = String.valueOf(pwview.getText());

                LoginPostCall postCall = new LoginPostCall(s1, s2);
                Log.e("exploit", "ID : " + s1 + ", PW : " + s2);
                Call call = postCall.createHttpPostMethodCall();

                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String verificationToken = response.headers().toString().split("set-cookie: ")[1].split(";")[0].replace("__RequestVerificationToken=","");
                        Log.e("==exploit==", "==="+verificationToken);
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
                //Toast.makeText(getApplicationContext(), "mail : " + s1 + ", " + "pw : " + s2, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
