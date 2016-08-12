package com.myemcu.app_18sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by Administrator on 2016/8/12.
 */
public class MyDBHelper extends SQLiteOpenHelper {

    /*final String CREATE_TABLE_SQL = // 创建名为mytable的数据表
            "create table memento_db(_id integer primary"+"key autoincrement, mSubject, mBody, mDate)";*/

    final String CREATE_TABLE_SQL=
            "create table myTable_db(_id integer primary " +
                    "key autoincrement,subject,body,date)";

    // 构造器
    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("--"+oldVersion+"--"+newVersion);
    }
}
