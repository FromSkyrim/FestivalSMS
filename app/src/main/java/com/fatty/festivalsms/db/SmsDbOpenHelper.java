package com.fatty.festivalsms.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fatty.festivalsms.bean.SentMsg;

/**
 * Created by 17255 on 2016/6/17.
 */
public class SmsDbOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "sms.db";
    private static final int DB_version = 1;

    private static SmsDbOpenHelper mSmsDbOpenHelper;

    private SmsDbOpenHelper(Context context) {
        super(context.getApplicationContext(), DB_NAME, null, DB_version);
    }

    public static SmsDbOpenHelper getInstance(Context context) {
        if (mSmsDbOpenHelper == null) {
            synchronized (SmsDbOpenHelper.class) {
                if (mSmsDbOpenHelper == null) {
                    mSmsDbOpenHelper = new SmsDbOpenHelper(context);
                }
            }
        }
        return mSmsDbOpenHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + SentMsg.TABLE_NAME + "(" +
                " _id integer primary key autoincrement , " +
                SentMsg.COLUMN_DATE + " integer , " +
                SentMsg.COLUMN_FESTIVAL_NAME + " text , " +
                SentMsg.COLUMN_MSG + " text , " +
                SentMsg.COLUMN_NUMBER + " text , " +
                SentMsg.COLUMN_NAME + " text " + ")";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
