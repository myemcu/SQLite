package com.myemcu.app_18sqlite;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Administrator on 2016/8/16.
 */

public class APP_18_Mementos {
    public static final String AUTHORITY = "com.myemcu.app_18sqlite.APP_18_Mementos";

    public static final class Memento implements BaseColumns {  // 参考字段在MainActivity的L108

        public static final String _ID = "_id";                 // memento_tb表中_id字段
        public static final String SUBJECT = "subject";         // memento_tb表中subject字段
        public static final String BODY = "body";               // memento_tb表中bodyt字段
        public static final String DATE = "date";               // memento_tb表中date字段

        public static final Uri MEMENTOS_CONTENT_URI= Uri.parse("content://" + AUTHORITY + "/mementos");// 提供操作mementos的集合URI
        public static final Uri MEMENTO_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/memento"); // 提供操作单个memento的URI
    }
}
