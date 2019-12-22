package com.atzandroid.weektasks;

import android.database.Cursor;

import static com.atzandroid.weektasks.DbConstants.WEEK_TASKS_TABLE_ALARM_TIME;
import static com.atzandroid.weektasks.DbConstants.WEEK_TASKS_TABLE_BODY;
import static com.atzandroid.weektasks.DbConstants.WEEK_TASKS_TABLE_DAY;
import static com.atzandroid.weektasks.DbConstants.WEEK_TASKS_TABLE_HAS_ALARM;
import static com.atzandroid.weektasks.DbConstants.WEEK_TASKS_TABLE_PK;
import static com.atzandroid.weektasks.DbConstants.WEEK_TASKS_TABLE_TITLE;
import static com.atzandroid.weektasks.DbConstants.WEEK_TASKS_TABLE_TO_DO_TIME;

public class MyTask {
    String title, body, toDoTime, alarmTime;
    int pk, day, has_alarm;

    public MyTask(Cursor cursor) {
        this.title = cursor.getString(cursor.getColumnIndex(WEEK_TASKS_TABLE_TITLE));
        this.body = cursor.getString(cursor.getColumnIndex(WEEK_TASKS_TABLE_BODY));
        this.toDoTime = cursor.getString(cursor.getColumnIndex(WEEK_TASKS_TABLE_TO_DO_TIME));
        this.alarmTime = cursor.getString(cursor.getColumnIndex(WEEK_TASKS_TABLE_ALARM_TIME));
        this.pk = cursor.getInt(cursor.getColumnIndex(WEEK_TASKS_TABLE_PK));
        this.day = cursor.getInt(cursor.getColumnIndex(WEEK_TASKS_TABLE_DAY));
        this.has_alarm = cursor.getInt(cursor.getColumnIndex(WEEK_TASKS_TABLE_HAS_ALARM));;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getToDoTime() {
        return toDoTime;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public int getPk() {
        return pk;
    }

    public int getDay() {
        return day;
    }

    public int getHas_alarm() {
        return has_alarm;
    }
}
