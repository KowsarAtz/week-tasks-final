package com.atzandroid.weektasks;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class EditTaskFragment extends Fragment {

    private static final int NONE = -1;
    private static final String NOTIFICATION_CHANNEL_ID = "notif"
            , default_notification_channel_id = "default";

    private TextView titleTW, bodyTW, toDoTimeTWHour, editTaskDayNameTW,
            toDoTimeTWMinute, alarmTimeTWHour, alarmTimeTWMinute;
    private Button saveTaskBtn, cancelSaveTaskBtn;
    private FragmentTransaction mainActivityFragmentTransaction;
    static RoundedImageView taskeImage;

    static int activeObjectPK = 0;
    static String picturePath = "";
    static View globalView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_task, container, false);
        picturePath = "";
        globalView = view;

        titleTW = view.findViewById(R.id.taskTitleTextView);
        bodyTW = view.findViewById(R.id.taskBodyTextView);
        toDoTimeTWHour = view.findViewById(R.id.taskToDoTimeHourTextView);
        toDoTimeTWMinute = view.findViewById(R.id.taskToDoTimeMinuteTextView);
        alarmTimeTWHour = view.findViewById(R.id.taskAlarmTimeHourTextView);
        alarmTimeTWMinute = view.findViewById(R.id.taskAlarmTimeMinuteTextView);

        saveTaskBtn = view.findViewById(R.id.edit_task_save_btn);
        cancelSaveTaskBtn = view.findViewById(R.id.edit_task_cancel_btn);
        editTaskDayNameTW = view.findViewById(R.id.edit_task_day_name);

        taskeImage = view.findViewById(R.id.task_imageView);
        taskeImage.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onLongClick(View v) {
                CameraActivity.title = "Task Title: " + titleTW.getText().toString().trim();
                startActivity(new Intent(getContext(), CameraActivity.class));
                return false;
            }
        });

        mainActivityFragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

        saveTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!createOrUpdateTask())
                    return;
                getBackToPrevFragment();
            }
        });

        cancelSaveTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBackToPrevFragment();
            }
        });
        setContents();
        return view;
    }

    public static void updateImage(Activity activity){
        if (!picturePath.equals("")) {
            File file = new File(picturePath);
            taskeImage.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        }else{
            taskeImage.setImageDrawable(globalView.getResources().getDrawable(R.drawable.camera_logo));
        }
        if (activity != null)
            activity.finish();
    }

    private void setContents(){
        editTaskDayNameTW.setText(MainActivity.daysFullname[MainActivity.lastActiveFragmentDay]);
        if(activeObjectPK==0)
            return;
        MyTask task = (new WeekTasksDBHelper(getActivity())).getMyTask(activeObjectPK);
        titleTW.setText(task.getTitle());
        bodyTW.setText(task.getBody());
        String[] toDoTimeParts = task.getToDoTime().split(":");
        if(toDoTimeParts.length == 2) {
            toDoTimeTWHour.setText(String.valueOf(toDoTimeParts[0]));
            toDoTimeTWMinute.setText(String.valueOf(toDoTimeParts[1]));
        }
        String[] alarmTimeParts = task.getAlarmTime().split(":");
        if(alarmTimeParts.length == 2) {
            alarmTimeTWHour.setText(String.valueOf(alarmTimeParts[0]));
            alarmTimeTWMinute.setText(String.valueOf(alarmTimeParts[1]));
        }
        picturePath = task.getPicturePath();
        updateImage(null);
    }

    private void getBackToPrevFragment(){
        ((MainActivity) getActivity()).updateFragment(MainActivity.lastActiveFragmentDay);
    }

    public Boolean createOrUpdateTask(){

        if (MainActivity.lastActiveFragmentDay == NONE){
            Toast.makeText(getActivity(), "Invalid Day!",Toast.LENGTH_LONG).show();
            return Boolean.FALSE;
        }
        String title = titleTW.getText().toString().trim();
        if(title.length() == 0){
            Toast.makeText(getActivity(), "Empty Title! Try Again!",Toast.LENGTH_LONG).show();
            return Boolean.FALSE;
        }
        String body = bodyTW.getText().toString().trim();

        int toDoTimeMinuteInt;
        int toDoTimeHourInt;
        try{
            toDoTimeMinuteInt = Integer.parseInt(toDoTimeTWMinute.getText().toString().trim());
            toDoTimeHourInt = Integer.parseInt(toDoTimeTWHour.getText().toString().trim());
            if (toDoTimeHourInt >= 24 || toDoTimeHourInt<0 || toDoTimeMinuteInt >= 60 || toDoTimeMinuteInt<0)
                throw new Exception();
        }catch (Exception e){
            Toast.makeText(getActivity(), "Invalid To Do Time! Try Again!",Toast.LENGTH_LONG).show();
            return Boolean.FALSE;
        }


        String alarmTimeMinute = alarmTimeTWMinute.getText().toString().trim();
        String alarmTimeHour = alarmTimeTWHour.getText().toString().trim();
        int hasAlarm = 0;
        int alarmTimeMinuteInt = 0;
        int alarmTimeHourInt = 0;
        if((alarmTimeMinute.length() == 0
                && alarmTimeHour.length() == 0)){
            //alarm not set
        }else{
            try{
                alarmTimeMinuteInt = Integer.parseInt(alarmTimeMinute);
                alarmTimeHourInt = Integer.parseInt(alarmTimeHour);
                if (alarmTimeHourInt >= 24 || alarmTimeHourInt<0 || alarmTimeMinuteInt >= 60 || alarmTimeMinuteInt<0)
                    throw new Exception();
                hasAlarm = 1;
            }catch (Exception e){
                Toast.makeText(getActivity(), "Invalid Alarm Time! Try Again!",Toast.LENGTH_LONG).show();
                return Boolean.FALSE;
            }
        }
        String alarmTime = (hasAlarm == 1 ? createTimeFormat(alarmTimeHourInt, alarmTimeMinuteInt) : "");
        WeekTasksDBHelper dbHelper = new WeekTasksDBHelper(getActivity());

        if(activeObjectPK != 0){
            (new WeekTasksDBHelper(getActivity())).deleteTask(activeObjectPK);
        }

        int notifID = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        if (hasAlarm == 1) {
            Boolean alarmResultOk;
            alarmResultOk = setAlarm(alarmTimeHourInt, alarmTimeMinuteInt, MainActivity.lastActiveFragmentDay, title, notifID);
            if(!alarmResultOk)
                return alarmResultOk;
        }
        dbHelper.createTask(title, body, createTimeFormat(toDoTimeHourInt, toDoTimeMinuteInt), MainActivity.lastActiveFragmentDay, hasAlarm, alarmTime, notifID, picturePath);
        if(activeObjectPK == 0)
            Toast.makeText(getActivity(), "New Task Created",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getActivity(), "Task Updated",Toast.LENGTH_LONG).show();

        return Boolean.TRUE;
    }

    public String createTimeFormat(int hour, int min){ //HH:MM
        String result = "",p1 = String.valueOf(hour),p2=String.valueOf(min);
        if(p1.length() == 1)
            p1 = "0"+p1;
        if(p2.length() == 1)
            p2 = "0"+p2;
        return p1+":"+p2;
    }

    private Boolean setAlarm(int hour, int minute, int day, String title, int notifID){
        int dayDiff = day - MainActivity.today;
        long milisecDiff = 0;
        Calendar now = Calendar.getInstance();
        Calendar deadline = Calendar.getInstance();
        deadline.set(Calendar.HOUR_OF_DAY, hour);
        deadline.set(Calendar.MINUTE, minute);
        deadline.set(Calendar.SECOND, 0);
        milisecDiff = deadline.getTimeInMillis() - now.getTimeInMillis() + dayDiff * 24 * 60 * 60 * 1000;
        if (milisecDiff < 0) {
            Toast.makeText(getActivity(), "Cannot set alarm in the past! \nTry Again",Toast.LENGTH_LONG).show();
            return Boolean.FALSE;
        }
        scheduleNotification(getNotification( title ) , (int) (milisecDiff/1000), notifID);
        return Boolean.TRUE;
    }

    private void scheduleNotification (Notification notification , int delayInt, int notifID) {
        long delay = delayInt * 1000;
        Intent notificationIntent = new Intent(getActivity(), MyNotificationPublisher.class) ;
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION_ID , notifID ) ;
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( getActivity(), 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
        long futureInMillis = SystemClock. elapsedRealtime () + delay ;
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE) ;
        assert alarmManager != null;
        alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , futureInMillis , pendingIntent) ;
    }
    private Notification getNotification (String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), default_notification_channel_id ) ;
        builder.setContentTitle( "Task Reminder" ) ;
        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable. ic_calendar) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId(NOTIFICATION_CHANNEL_ID) ;
        builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        return builder.build();
    }
}
