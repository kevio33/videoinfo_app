package com.kevin.videoinfo.DBhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBOpenHelper extends SQLiteOpenHelper {

    public static DBOpenHelper dbOpenHelper;

    private static int DBVersion;//保存数据库版本

    public static DBOpenHelper getDBOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version){
        if(dbOpenHelper == null){
            dbOpenHelper = new DBOpenHelper(context,name,factory,version);
            DBVersion = version;
        }

        return dbOpenHelper;
    }

    private DBOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //建表语句
        String sql_query = "create table user(id integer primary key autoincrement,username varchar(20),password varchar(20))";
        db.execSQL(sql_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
