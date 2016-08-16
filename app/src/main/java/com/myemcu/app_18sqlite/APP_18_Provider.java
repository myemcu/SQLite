package com.myemcu.app_18sqlite;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2016/8/16.
 */

public class APP_18_Provider extends ContentProvider{

    // 创建Uri匹配器对象
    private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    // 定义两个常量，用于匹配Uri的返回值
    private static final int MEMENTOS = 1;
    private static final int MEMENTO = 2;

    MyDBHelper dbHelper; // 自定义数据库对象
    SQLiteDatabase db;   // SQLite数据库对象

    // 添加URI匹配规则,用于判断URI的类型
    static {
        matcher.addURI(APP_18_Mementos.AUTHORITY, "mementos", MEMENTOS);
        matcher.addURI(APP_18_Mementos.AUTHORITY, "memento/#", MEMENTO);
    }
    //==============================================================================================
    @Override
    public boolean onCreate() {

        // 创建数据库
        dbHelper = new MyDBHelper(getContext(), "myTable.db", null,1);  // 参考MainAcyivity(L86)
        // 创建数据库工具类,并获取数据库实例
        db = dbHelper.getReadableDatabase();

        return true;
    }
    //----------------------------------------------------------------------------------------------
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (matcher.match(uri)) {
            case MEMENTOS:  // 处理集合
                    return db.query("myTable_db", projection, selection, selectionArgs, null, null, sortOrder);

            case MEMENTO:   // 处理单个
                    long id = ContentUris.parseId(uri);
                    String where = APP_18_Mementos.Memento._ID + "=" + id;
                    if (selection != null && !"".equals(selection)) {
                        where = where + " and " + selection;
                    }
                    return db.query("myTable_db", projection, where, selectionArgs, null, null, sortOrder);
            default:
                    throw new IllegalArgumentException("未知Uri：" + uri);
        }
    }
    //----------------------------------------------------------------------------------------------
    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (matcher.match(uri)) {
            case MEMENTOS:  return "vnd.android.cursor.dir/mementos";
            case MEMENTO:   return "vnd.android.cursor.item/memento";
            default:        throw new IllegalArgumentException("未知Uri：" + uri);
        }
    }
    //----------------------------------------------------------------------------------------------
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long rowID = db.insert("myTable_db", APP_18_Mementos.Memento._ID, values);

        // 如果添加成功，则通知数据库记录发生更新
        if (rowID > 0) {

            Uri mementoUri = ContentUris.withAppendedId(uri, rowID);
            getContext().getContentResolver().notifyChange(mementoUri, null);

            return mementoUri;
        }

        return null;
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int num = 0;    // 删除记录	，用于记录删除的记录数

        switch (matcher.match(uri)) {

            case MEMENTOS:  // 删除多条记录
                            num = db.delete("myTable_db", selection, selectionArgs);
                            break;

            case MEMENTO:   // 删除指定ID对应的记录
                            long id = ContentUris.parseId(uri); // 获取ID
                            String where = APP_18_Mementos.Memento._ID + "=" + id;  // ID字段需符合的条件
                            if (selection != null && !"".equals(selection)) {
                                where = where + " and " + selection;//拼接条件语句
                            }
                            num = db.delete("myTable_db", where, selectionArgs);
                            break;
            default:
                            throw new IllegalArgumentException("未知Uri：" + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);  // 通知变化

        return num;
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int num = 0;

        switch (matcher.match(uri)) {

            case MEMENTOS:
                            num = db.update("myTable_db", values, selection, selectionArgs);
                            break;

            case MEMENTO:
                            long id = ContentUris.parseId(uri);
                            String where = APP_18_Mementos.Memento._ID + "=" + id;
                            if (selection != null && !"".equals(selection)) {
                                where = where + " and " + selection;
                            }
                            num = db.update("myTable_db", values, where, selectionArgs);
                            break;

            default:
                            throw new IllegalArgumentException("未知Uri：" + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return num;
    }
}
