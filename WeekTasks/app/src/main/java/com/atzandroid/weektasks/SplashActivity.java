package com.atzandroid.weektasks;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class SplashActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://192.241.136.152:3000";
    public static final String GET_TOKEN = "/api/token/";
    public static final String APPROVE_TOKEN = "/api/user/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        validateToken(SplashActivity.this, false);
    }

    public static void validateToken(Activity activity, boolean isLoginActivity){
        String token = new WeekTasksDBHelper(activity).getLastToken();
        if(token.equals("")) {
            activity.startActivity(new Intent(activity, LoginActivity.class));
            activity.finish();
            return;
        }
        Ion.with(activity)
            .load(BASE_URL+APPROVE_TOKEN)
            .setHeader("Authorization", "Bearer "+token)
            .asJsonObject()
            .setCallback(new FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, JsonObject result) {
                    if (result != null && result.has("username")){
                        Bundle bundle = new Bundle();
                        bundle.putString("first_name", String.valueOf(result.get("first_name")));
                        bundle.putString("last_name", String.valueOf(result.get("last_name")));
                        bundle.putString("email", String.valueOf(result.get("email")));
                        bundle.putString("username", String.valueOf(result.get("username")));
                        Intent intent = new Intent(activity, MainActivity.class);
                        intent.putExtras(bundle);
                        if (isLoginActivity)
                            ((LoginActivity) activity).startAnimThread(intent);
                        else{
                            activity.startActivity(intent);
                            activity.finish();
                        }

                    } else {
                        new WeekTasksDBHelper(activity).updateLastToken("");
//                        Toast.makeText(activity, "Token has expired! Login Again", Toast.LENGTH_LONG).show();
                        activity.startActivity(new Intent(activity, LoginActivity.class));
                        activity.finish();
                    }
                }
            });
    }
}