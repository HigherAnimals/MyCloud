package com.higheranimals.mycloud.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    static final private String TAG = "DbHelper";
    static final private String DB_NAME = "mycloud.sqlite3";
    static final int DB_VERSION = 1;

    public final static String SERVICE_TABLE = "services";
    public final static String SERVICE_NAME = "name";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private static final String DB_CREATE = "CREATE TABLE " + SERVICE_TABLE
            + "(_id integer primary key autoincrement, " + SERVICE_NAME
            + " text not null);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }
}
