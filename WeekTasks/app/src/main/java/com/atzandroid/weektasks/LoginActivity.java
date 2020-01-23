package com.atzandroid.weektasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class LoginActivity extends AppCompatActivity {

    private Button loginBtn;
    private EditText usernameEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameTextView);
        passwordEditText = findViewById(R.id.passwordTextView);
        loginBtn = findViewById(R.id.login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeToken(usernameEditText.getText().toString().trim(), passwordEditText.getText().toString().trim());
            }
        });
    }

    private void storeToken(String username, String password) {
        JsonObject json = new JsonObject();
        json.addProperty("username", username);
        json.addProperty("password", password);
        Ion.with(this)
            .load(SplashActivity.BASE_URL+SplashActivity.GET_TOKEN)
            .setJsonObjectBody(json)
            .asJsonObject()
            .setCallback(new FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, JsonObject result) {
                    if (result != null && result.has("access")) {
                        String token = String.valueOf(result.get("access")).replaceAll("^\"|\"$", "");
                        new WeekTasksDBHelper(LoginActivity.this).updateLastToken(token);
                        SplashActivity.validateToken(LoginActivity.this);
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid Username or Password! Try Again", Toast.LENGTH_LONG).show();
                    }
                }
            });
    }
}
