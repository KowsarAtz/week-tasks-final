package com.atzandroid.weektasks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class KeypadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keypad);

        WeekTasksDBHelper dbHelper = new WeekTasksDBHelper(this);

        if (dbHelper.isPassproteced())
            Toast.makeText(this, "passprotected!", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "not passprotected!", Toast.LENGTH_LONG).show();

    }
}
