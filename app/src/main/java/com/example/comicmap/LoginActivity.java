package com.example.comicmap;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

                Toast.makeText(getApplicationContext(), "mail : " + s1 + ", " + "pw : " + s2, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
