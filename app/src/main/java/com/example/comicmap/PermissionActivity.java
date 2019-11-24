package com.example.comicmap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.comicmap.OAuth.TokenClient;
import com.example.comicmap.OAuth.TokenProcess;

import org.json.JSONObject;

import java.util.HashMap;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class PermissionActivity extends AppCompatActivity {
    private Button button;
    private LoginSharedPreference loginSharedPreference = new LoginSharedPreference();
    private TokenProcess tokenInterface = TokenClient.getClient(TokenProcess.BASE_URL).create(TokenProcess.class);
    private retrofit2.Call<ResponseBody> responseBodyCall;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_permission);
            button = findViewById(R.id.permission_button);
            button.setOnClickListener(v -> {
                button.setEnabled(false);
                permission_check();
            });
        }

    //Permission Check : check Gold User & get Token for API
    public void permission_check() {
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

                    //Check user with elapsedTime... f**k..
                    Handler timer = new Handler();
                    timer.postDelayed(() -> createToken(), 1500);
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Network Error! Retry Please..", Toast.LENGTH_SHORT).show();
                button.setEnabled(true);
            }
        });
    }

    //IF Checked Gold User.. get Code
    public void createToken() {
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
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Network Error! Retry Please..", Toast.LENGTH_SHORT).show();
                        button.setEnabled(true);
                    }
                });
    }

    //After get Code, access API & get Token
    public void accessToken() {
        Log.e("exploit", "============= access Token...================");
                HashMap<String, Object> postData2 = new HashMap<>();
                postData2.put("grant_type", "authorization_code");
                postData2.put("code", loginSharedPreference.getString("tokenCode"));
                postData2.put("client_id", "comicmapgZwp98BPmh5rj35zfnFNcZA5mxrpyCUQ");
                postData2.put("client_secret", "bGLDLnC7NrwFnWR3a8C2hz9sYEJtcnLhMwRJHdwV");
                responseBodyCall = tokenInterface.accessToken(postData2);
                responseBodyCall.enqueue(new retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        try {
                            String result = response.body().string();
                            Log.e("exploit", "Token retrieved! : " + result);

                            JSONObject json = new JSONObject(result);
                            String access_token = json.getString("access_token");
                            String refresh_token = json.getString("refresh_token");
                            loginSharedPreference.putString("access_token", access_token);
                            loginSharedPreference.putString("refresh_token", refresh_token);
                            Log.e("exploit", "Token retrieved! : " + access_token + ", " + refresh_token);
                            startActivity(new Intent(PermissionActivity.this, MainActivity.class));
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Network Error! Retry Please..", Toast.LENGTH_SHORT).show();
                        button.setEnabled(true);
                    }
                });
        }
}
