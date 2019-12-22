package com.atzandroid.weektasks;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import static com.atzandroid.weektasks.DbConstants.CREATE_TABLE_PARAMS;
import static com.atzandroid.weektasks.DbConstants.CREATE_TABLE_WEEK_TASKS;
import static com.atzandroid.weektasks.DbConstants.DB_NAME;
import static com.atzandroid.weektasks.DbConstants.DB_VERSION;
import static com.atzandroid.weektasks.DbConstants.DROP_TABLES;
import static com.atzandroid.weektasks.DbConstants.INIT_TABLE_PARAMS_1;
import static com.atzandroid.weektasks.DbConstants.INIT_TABLE_PARAMS_2;
import static com.atzandroid.weektasks.DbConstants.INIT_TABLE_PARAMS_3;
import static com.atzandroid.weektasks.DbConstants.NOT_SET;
import static com.atzandroid.weektasks.DbConstants.PARAM_TABLE;
import static com.atzandroid.weektasks.DbConstants.PARAM_TABLE_ITEM_PK;
import static com.atzandroid.weektasks.DbConstants.PARAM_TABLE_ITEM_VALUE;
import static com.atzandroid.weektasks.DbConstants.PASSWORD_HASH;
import static com.atzandroid.weektasks.DbConstants.PASS_PROTECTED_STATUS;
import static com.atzandroid.weektasks.DbConstants.WEEK_TASKS_TABLE;

public class WeekTasksDBHelper extends SQLiteOpenHelper {

    public WeekTasksDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PARAMS);
        db.execSQL(INIT_TABLE_PARAMS_1);
        db.execSQL(INIT_TABLE_PARAMS_2);
        db.execSQL(INIT_TABLE_PARAMS_3);
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

//    public void test(){
//        SQLiteDatabase db = getWritableDatabase();
//        return;
//    }

}
