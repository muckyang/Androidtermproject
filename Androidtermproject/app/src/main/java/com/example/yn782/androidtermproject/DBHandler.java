package com.example.yn782.androidtermproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by yn782 on 2016-11-05.
 */

public class DBHandler extends SQLiteOpenHelper{

    private static final String DB_NAME="ex12"; //꼭 같이 변경해줘야함
   private static final int DATABASE_VERSION = 1;
    public DBHandler(Context context) {

        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ex12 ("+
        "PID INTEGER NOT null primary key,"+
        "title TEXT NULL,"+
        "stime TEXT NULL,"+
        "endtime TEXT NULL,"+
        "place TEXT NULL,"+
        "year INTEGER NULL,"+
        "month INTEGER NULL,"+
        "day INTEGER NULL,"+
        "nowtime TEXT NULL"+
        ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ex11");
        onCreate(db);
    }
}
