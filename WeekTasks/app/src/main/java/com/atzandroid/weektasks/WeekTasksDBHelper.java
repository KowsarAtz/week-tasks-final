package com.atzandroid.weektasks;
import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import static com.atzandroid.weektasks.DbConstants.CREATE_TABLE_PARAMS;
import static com.atzandroid.weektasks.DbConstants.CREATE_TABLE_STRING_PARAMS;
import static com.atzandroid.weektasks.DbConstants.CREATE_TABLE_WEEK_TASKS;
import static com.atzandroid.weektasks.DbConstants.DB_NAME;
import static com.atzandroid.weektasks.DbConstants.DB_VERSION;
import static com.atzandroid.weektasks.DbConstants.DROP_TABLES;
import static com.atzandroid.weektasks.DbConstants.INIT_TABLE_PARAMS_1;
import static com.atzandroid.weektasks.DbConstants.INIT_TABLE_PARAMS_2;
import static com.atzandroid.weektasks.DbConstants.INIT_TABLE_PARAMS_3;
import static com.atzandroid.weektasks.DbConstants.INIT_TABLE_PARAMS_4;
import static com.atzandroid.weektasks.DbConstants.INIT_TABLE_STRING_PARAMS_1;
import static com.atzandroid.weektasks.DbConstants.LAST_DAY_VISTIED;
import static com.atzandroid.weektasks.DbConstants.LAST_TOKEN;
import static com.atzandroid.weektasks.DbConstants.NOT_SET;
import static com.atzandroid.weektasks.DbConstants.OVER_DUE_STATE;
import static com.atzandroid.weektasks.DbConstants.PARAM_TABLE;
import static com.atzandroid.weektasks.DbConstants.PARAM_TABLE_ITEM_PK;
import static com.atzandroid.weektasks.DbConstants.PARAM_TABLE_ITEM_VALUE;
import static com.atzandroid.weektasks.DbConstants.PASSWORD_HASH;
import static com.atzandroid.weektasks.DbConstants.PASS_PROTECTED_STATUS;
import static com.atzandroid.weektasks.DbConstants.STRING_PARAM_TABLE;
import static com.atzandroid.weektasks.DbConstants.STRING_PARAM_TABLE_ITEM_PK;
import static com.atzandroid.weektasks.DbConstants.STRING_PARAM_TABLE_ITEM_VALUE;
import static com.atzandroid.weektasks.DbConstants.TO_DO_STATE;
import static com.atzandroid.weektasks.DbConstants.WEEK_TASKS_TABLE;
import static com.atzandroid.weektasks.DbConstants.WEEK_TASKS_TABLE_ALARM_TIME;
import static com.atzandroid.weektasks.DbConstants.WEEK_TASKS_TABLE_BODY;
import static com.atzandroid.weektasks.DbConstants.WEEK_TASKS_TABLE_DAY;
import static com.atzandroid.weektasks.DbConstants.WEEK_TASKS_TABLE_HAS_ALARM;
import static com.atzandroid.weektasks.DbConstants.WEEK_TASKS_TABLE_NOTIFICATION_ID;
import static com.atzandroid.weektasks.DbConstants.WEEK_TASKS_TABLE_PK;
import static com.atzandroid.weektasks.DbConstants.WEEK_TASKS_TABLE_STATE;
import static com.atzandroid.weektasks.DbConstants.WEEK_TASKS_TABLE_TITLE;
import static com.atzandroid.weektasks.DbConstants.WEEK_TASKS_TABLE_TO_DO_TIME;

public class WeekTasksDBHelper extends SQLiteOpenHelper {

    private Context dbContext;

    public WeekTasksDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        dbContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PARAMS);
        db.execSQL(INIT_TABLE_PARAMS_1);
        db.execSQL(INIT_TABLE_PARAMS_2);
        db.execSQL(INIT_TABLE_PARAMS_3);
        db.execSQL(INIT_TABLE_PARAMS_4);
        db.execSQL(CREATE_TABLE_STRING_PARAMS);
        db.execSQL(INIT_TABLE_STRING_PARAMS_1);
        Log.i(DB_NAME, PARAM_TABLE + " table created and initialized.");
        db.execSQL(CREATE_TABLE_WEEK_TASKS);
        Log.i(DB_NAME, WEEK_TASKS_TABLE + " table created and initialized.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLES);
        Log.i(DB_NAME, "tables dropped, calling on create . . .");
        onCreate(db);
    }

    public String getLastToken(){
        String temp;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+ STRING_PARAM_TABLE_ITEM_VALUE +" FROM "+ STRING_PARAM_TABLE +" " +
                "WHERE "+ STRING_PARAM_TABLE_ITEM_PK +" = "+LAST_TOKEN, null);
        cursor.moveToFirst();
        temp = cursor.getString(0);
        db.close();
        cursor.close();
        return temp;
    }

    public void updateLastToken(String newToken){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE "+ STRING_PARAM_TABLE +" SET "+ STRING_PARAM_TABLE_ITEM_VALUE +"='"+
                newToken +"' WHERE "+ STRING_PARAM_TABLE_ITEM_PK+"=" + LAST_TOKEN);
        db.close();
    }

    public int passProtectedStatus(){
        int temp;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+ PARAM_TABLE_ITEM_VALUE +" FROM "+ PARAM_TABLE +" " +
                "WHERE "+ PARAM_TABLE_ITEM_PK +" = "+String.valueOf(PASS_PROTECTED_STATUS), null);
        cursor.moveToFirst();
        temp = cursor.getInt(0);
        db.close();
        cursor.close();
        return temp;
    }

    public void changePassProtectedStatus(int newstatus){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE "+ PARAM_TABLE +" SET "+ PARAM_TABLE_ITEM_VALUE +"="+
                String.valueOf(newstatus)+" WHERE "+PARAM_TABLE_ITEM_PK+"=" +
                String.valueOf(PASS_PROTECTED_STATUS));
        db.close();
    }

    public void changePassword(int newpass){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE "+ PARAM_TABLE +" SET "+ PARAM_TABLE_ITEM_VALUE +"="+
                String.valueOf(newpass)+" WHERE "+PARAM_TABLE_ITEM_PK+"=" +
                String.valueOf(PASSWORD_HASH));
        db.close();
    }

    public int getPassHash(){
        int temp;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+ PARAM_TABLE_ITEM_VALUE +" FROM "+ PARAM_TABLE +" " +
                "WHERE "+ PARAM_TABLE_ITEM_PK +" = "+String.valueOf(PASSWORD_HASH), null);
        cursor.moveToFirst();
        temp = cursor.getInt(0);
        db.close();
        cursor.close();
        return temp;
    }

    public int getLastVisited(){
        int temp;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+ PARAM_TABLE_ITEM_VALUE +" FROM "+ PARAM_TABLE +" " +
                "WHERE "+ PARAM_TABLE_ITEM_PK +" = "+String.valueOf(LAST_DAY_VISTIED), null);
        cursor.moveToFirst();
        temp = cursor.getInt(0);
        db.close();
        cursor.close();
        return temp;
    }

    public void setLastVisited(int today){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE "+ PARAM_TABLE +" SET "+ PARAM_TABLE_ITEM_VALUE +"="+
                String.valueOf(today)+" WHERE "+PARAM_TABLE_ITEM_PK+"=" +
                String.valueOf(LAST_DAY_VISTIED));
        db.close();
    }

    public void deleteAllTask(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+ WEEK_TASKS_TABLE);
        if(db.isOpen())
            db.close();
    }

    public void createTask(String title, String body, String toDoTime, int day, int hasAlarm, String alarmTime, int notifID){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO " + WEEK_TASKS_TABLE
                + " (" + WEEK_TASKS_TABLE_TITLE
                + " , " + WEEK_TASKS_TABLE_BODY + " , " + WEEK_TASKS_TABLE_TO_DO_TIME
                + " , " + WEEK_TASKS_TABLE_ALARM_TIME
                + " , " + WEEK_TASKS_TABLE_DAY
                + " , " + WEEK_TASKS_TABLE_STATE
                + " , " + WEEK_TASKS_TABLE_NOTIFICATION_ID
                + " , " + WEEK_TASKS_TABLE_HAS_ALARM +") "
                + "VALUES " + "('" + title + "' , '" + body
                + "' , '" + toDoTime
                + "' , '" + alarmTime + "' , "
                + day + " , " + TO_DO_STATE + " , " + notifID + " , " + hasAlarm + ")");
        db.close();
        Log.i(DB_NAME, "new task created . . .");
    }

    public ArrayList<MyTask> getDayTasks(int day, int status){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<MyTask> tasks = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                + WEEK_TASKS_TABLE
                +" WHERE "+ WEEK_TASKS_TABLE_DAY + "=" + day + " and " + WEEK_TASKS_TABLE_STATE + "=" + status
                , null);
        if(cursor.moveToNext()) {
            do {
                tasks.add(new MyTask(cursor));
            } while (cursor.moveToNext());
        }
        if(db.isOpen()) db.close();
        cursor.close();
        return tasks;
    }

    public void deleteTask(int pk){
        SQLiteDatabase db = getWritableDatabase();
        int temp;
        Cursor cursor = db.rawQuery("SELECT "+ WEEK_TASKS_TABLE_NOTIFICATION_ID +" FROM "+ WEEK_TASKS_TABLE +" " +
                "WHERE "+ WEEK_TASKS_TABLE_PK +" = "+pk, null);
        cursor.moveToFirst();
        temp = cursor.getInt(0);
        ((NotificationManager)dbContext.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(temp);
        db.execSQL("DELETE FROM "+ WEEK_TASKS_TABLE +" WHERE "+ WEEK_TASKS_TABLE_PK +"="+pk);
        db.close();
        cursor.close();
    }

    public MyTask getMyTask (int pk){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ WEEK_TASKS_TABLE +" WHERE "+ WEEK_TASKS_TABLE_PK +"="+pk, null);
        MyTask task = null;
        if(cursor.moveToNext())
            task = new MyTask(cursor);
        if(db.isOpen()) db.close();
        cursor.close();
        return task;
    }

    public void updateTaskState(int pk, int newNewState){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE "+ WEEK_TASKS_TABLE +" SET "+ WEEK_TASKS_TABLE_STATE +"="+newNewState+" WHERE "+ WEEK_TASKS_TABLE_PK +"="+pk);
        if(db.isOpen())
            db.close();
    }

    public void overDuePreviousTasks(int today){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE "+ WEEK_TASKS_TABLE +" SET "+ WEEK_TASKS_TABLE_STATE +"="+OVER_DUE_STATE
                +" WHERE "+ WEEK_TASKS_TABLE_DAY +"<"+today+" and "+WEEK_TASKS_TABLE_STATE+"="+TO_DO_STATE);
        if(db.isOpen())
            db.close();
    }

}
