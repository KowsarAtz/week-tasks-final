package com.atzandroid.weektasks;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import static com.atzandroid.weektasks.DbConstants.CREATE_TABLE_PARAMS;
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
import static com.atzandroid.weektasks.DbConstants.PASS_PROTECTED_STATUS;

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLES);
        Log.i(DB_NAME, "tables dropped, calling on create . . .");
        onCreate(db);
    }

    public Boolean isPassproteced(){
        Boolean temp = Boolean.TRUE;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+ PARAM_TABLE_ITEM_VALUE +" FROM "+ PARAM_TABLE +" " +
                "WHERE "+ PARAM_TABLE_ITEM_PK +" = "+String.valueOf(PASS_PROTECTED_STATUS), null);
        cursor.moveToFirst();
        if (cursor.getInt(0) != 1)
            temp = Boolean.FALSE;
        db.close();
        cursor.close();
        return temp;
    }
}
