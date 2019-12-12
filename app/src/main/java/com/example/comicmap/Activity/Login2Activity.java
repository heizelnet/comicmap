package com.example.comicmap.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;


public class Login2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String REDIRECT_URI_ROOT="comicmap.login://redirect";
        String CODE = "code";
        String STATE = "state";
        String ERROR_CODE = "error_code";
        String ERROR = "error";
        Log.e("exploit", "Login2Activity Called!");

        Uri data = getIntent().getData();
        Log.e("exploit", "data : " + data.getQueryParameterNames());

        if (data != null && !TextUtils.isEmpty(data.getScheme())) {
            String code = data.getQueryParameter(CODE);
            String state = data.getQueryParameter(STATE);
            String error_code = data.getQueryParameter(ERROR_CODE);
            String error = data.getQueryParameter(ERROR);
            if (!TextUtils.isEmpty(code)) {
                Log.e("exploit", "code : " + code + ", state : " + state);
            }
            if(!TextUtils.isEmpty(error)) {
                //Not Gold User.. or Not Permmited...
                Log.e("exploit", "onCreate: handle result of authorization with error :" + error);
                //then die
            } if(!TextUtils.isEmpty(error_code)) {
                //a problem occurs, the user reject our granting request or something like that
                Log.e("exploit", "onCreate: handle result of authorization with error :" + error_code);
                //then die
            }
        }
    }
}
