package com.higheranimals.mycloud.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    static final private String TAG = "DbHelper";
    static final private String DB_NAME = "mycloud.sqlite3";
    static final int DB_VERSION = 1;

    // Service table constants
    public final static String SERVICE_TABLE = "services";
    public final static String SERVICE_NAME = "sv_name";
    public final static String SERVICE_TYPE = "sv_type";

    // Account table constants
    public final static String ACCOUNT_TABLE = "accounts";
    public final static String ACCOUNT_NAME = "ac_name";
    public final static String ACCOUNT_NO = "ac_number";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private static final String SERVICE_TABLE_CREATE = "CREATE TABLE "
            + SERVICE_TABLE + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SERVICE_NAME + " TEXT NOT NULL, " + SERVICE_TYPE
            + " TEXT NOT NULL);";
    private static final String ACCOUNT_TABLE_CREATE = "CREATE TABLE "
            + ACCOUNT_TABLE + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ACCOUNT_NO + " TEXT UNIQUE NOT NULL, " + ACCOUNT_NAME
            + " TEXT UNIQUE NOT NULL);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SERVICE_TABLE_CREATE);
        db.execSQL(SERVICE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }
}
