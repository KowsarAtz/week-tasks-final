package com.atzandroid.weektasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class EditTaskFragment extends Fragment {

    private static final int NONE = -1;

    private TextView titleTW, bodyTW, toDoTimeTWHour,
            toDoTimeTWMinute, alarmTimeTWHour, alarmTimeTWMinute;
    private Button saveTaskBtn, cancelSaveTaskBtn;
    private FragmentTransaction mainActivityFragmentTransaction;

    static Boolean createMode = Boolean.TRUE;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_task, container, false);

        titleTW = view.findViewById(R.id.taskTitleTextView);
        bodyTW = view.findViewById(R.id.taskBodyTextView);
        toDoTimeTWHour = view.findViewById(R.id.taskToDoTimeHourTextView);
        toDoTimeTWMinute = view.findViewById(R.id.taskToDoTimeMinuteTextView);
        alarmTimeTWHour = view.findViewById(R.id.taskAlarmTimeHourTextView);
        alarmTimeTWMinute = view.findViewById(R.id.taskAlarmTimeMinuteTextView);

        saveTaskBtn = view.findViewById(R.id.edit_task_save_btn);
        cancelSaveTaskBtn = view.findViewById(R.id.edit_task_cancel_btn);


        mainActivityFragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

        saveTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // to be completed ... (editing or creating?)
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

        return view;
    }

    private void getBackToPrevFragment(){
        if (mainActivityFragmentTransaction == null)
            return;
        if (MainActivity.today == MainActivity.lastActiveFragmentDay){
            mainActivityFragmentTransaction.replace(R.id.day_activities_fragment, new TodayFragment());
            mainActivityFragmentTransaction.commit();
            return;
        }
        mainActivityFragmentTransaction.replace(R.id.day_activities_fragment, new NextDayFragment());
        mainActivityFragmentTransaction.commit();
    }

    public Boolean createOrUpdateTask(){

        if(!createMode){
            //to be competed . . . delete old one (postpone) and create a new one
        }

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
        if (alarmTimeMinute.length() != 0 && alarmTimeHour.length() != 0){
            hasAlarm = 1;
            //to be completed . . . (set alarm)
        }else if((alarmTimeMinute.length() == 0 && alarmTimeHour.length() == 0)){
            // pass
        }else if((alarmTimeMinute.length() == 0 || alarmTimeHour.length() == 0)){
            Toast.makeText(getActivity(), "Invalid Alarm Time! Try Again!",Toast.LENGTH_LONG).show();
            return Boolean.FALSE;
        }


        int alarmTimeMinuteInt = 0;
        int alarmTimeHourInt = 0;
        if((alarmTimeMinute.length() == 0 && alarmTimeHour.length() == 0)){
            //pass
        }else{
            try{
                alarmTimeMinuteInt = Integer.parseInt(alarmTimeTWMinute.getText().toString().trim());
                alarmTimeHourInt = Integer.parseInt(alarmTimeTWHour.getText().toString().trim());
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
        dbHelper.createTask(title, body, createTimeFormat(toDoTimeHourInt, toDoTimeMinuteInt), MainActivity.lastActiveFragmentDay, hasAlarm, alarmTime); //to be completed . . .(day)

        if(createMode)
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
}
