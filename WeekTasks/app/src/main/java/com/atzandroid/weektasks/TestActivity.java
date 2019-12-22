package com.atzandroid.weektasks;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task);

//        testAlarmManager();
//        cancelAlarmManager();
//
//        TextView tv = findViewById(R.id.testTextView);
//        tv.setText(Calendar.getInstance().getTime().toString());

    }

    private void cancelAlarmManager() {

        AlarmManager alarmMgr;
        PendingIntent alarmIntent;

        alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyTestAlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);


        try {
            alarmMgr.cancel(alarmIntent);
            Toast.makeText(this, "Canceled it!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Didn't Cancel it!", Toast.LENGTH_LONG).show();
        }
    }

    private void testAlarmManager() {
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;

        alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyTestAlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 11);

        assert alarmMgr != null;

        // repeat every 20 minutes
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 20, alarmIntent);

        Toast.makeText(this, "set at 1 11!", Toast.LENGTH_LONG).show();
    }
}
