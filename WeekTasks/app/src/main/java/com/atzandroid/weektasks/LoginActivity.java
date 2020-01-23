package com.atzandroid.weektasks;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.media.ImageReader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class LoginActivity extends AppCompatActivity {

    private Button loginBtn;
    private EditText usernameEditText, passwordEditText;
    private CircularProgressBar circularProgressBar;
    private ImageView tickImage;
    private LinearLayout logginFieldsLayout;
    private TextView loggedInMsgTw;
    private FrameLayout tickAndCustomFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tickAndCustomFrame = findViewById(R.id.tick_and_custom_frame);
        loggedInMsgTw = findViewById(R.id.successful_log_tw);
        logginFieldsLayout = findViewById(R.id.loggin_fields_layout);
        tickImage = findViewById(R.id.loggin_tick_iv);
        usernameEditText = findViewById(R.id.usernameTextView);
        passwordEditText = findViewById(R.id.passwordTextView);
        loginBtn = findViewById(R.id.login_btn);
        circularProgressBar = findViewById(R.id.progress_circular);

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
                        SplashActivity.validateToken(LoginActivity.this, true);
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid Username or Password! Try Again", Toast.LENGTH_LONG).show();
                    }
                }
            });
    }

    public void startAnimThread(Intent intent) {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                int totalSteps = 180;
                float step = 4000/totalSteps;
                float stepAngle = 360/totalSteps;
                try {
                    for (int i=1; i<=totalSteps; i+=1) {
                            Thread.sleep((long) step);
                            circularProgressBar.update(stepAngle);
                            circularProgressBar.postInvalidate();
                    }
                    circularProgressBar.setFinished();
                    circularProgressBar.postInvalidate();
                    loggedInMsgTw.setTextColor(LoginActivity.this.getResources().getColor(R.color.green_approved));
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                startActivity(intent);
                finish();
            }
        });
        logginFieldsLayout.animate().setDuration(1000).alpha(0).translationY(-1000).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                thread.start();
                logginFieldsLayout.setVisibility(View.GONE);
                loggedInMsgTw.animate().setDuration(4000).alpha(1).setListener(null).start();
                tickAndCustomFrame.setVisibility(View.VISIBLE);
                tickImage.animate().setDuration(4000).alpha(1f).setListener(null).start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();

    }
}
