package com.myemcu.app_18sqlite;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Button mChooseDate;
    private EditText mDate;
    private MyDBHelper myDBHelper;
    private SQLiteDatabase db;
    private EditText mSubject;
    private EditText mBody;
    //private EditText mDate1;
    private LinearLayout mTitle;
    private ListView mResult;
    private Cursor mCursor;

    private MyOnClickListerner myOnClikListerner;
    private Button mAdd;
    private Button mQry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mChooseDate = (Button) findViewById(R.id.chooseDate);
        mDate = (EditText) findViewById(R.id.date);

        mSubject = (EditText) findViewById(R.id.subject);
        mBody = (EditText) findViewById(R.id.body);

        mTitle = (LinearLayout) findViewById(R.id.title);
        mResult = (ListView) findViewById(R.id.result);

        mAdd = (Button) findViewById(R.id.add);
        mQry = (Button) findViewById(R.id.query);

        mTitle.setVisibility(View.INVISIBLE);       // 隐藏表头

        mChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();    // 获取当前日期

                // 弹出日期对话框
                new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() { // 监听日期对话框
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                mDate.setText(year+"-"+(month+1)+"-"+day); // 设置日期格式
                            }
                        },
                        c.get(Calendar.YEAR),   // 获取年
                        c.get(Calendar.MONTH),  // 获取月
                        c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        myOnClikListerner = new MyOnClickListerner();
        mAdd.setOnClickListener(myOnClikListerner);
        mQry.setOnClickListener(myOnClikListerner);
    }

    // 自定义(点击)事件监听器(内部类)
    private class MyOnClickListerner implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            myDBHelper = new MyDBHelper(MainActivity.this,"myTable.db",null,1); // 创建数据库辅助类
            db = myDBHelper.getReadableDatabase();                              // 获取SQLite数据库

            String subStr  = mSubject.getText().toString();                      // 获取编辑框内容
            String bodyStr = mBody.getText().toString();
            String dateStr = mDate.getText().toString();

            switch (v.getId()) {
                case R.id.add:
                                mTitle.setVisibility(View.INVISIBLE);       // 隐藏表头
                                addMemento(db, subStr, bodyStr, dateStr);   // 调用添加记录的方法
                                Toast.makeText(MainActivity.this,"添加成功",Toast.LENGTH_LONG).show();
                                mResult.setAdapter(null);                   // 下拉列表为空
                                break;

                case R.id.query:
                                mTitle.setVisibility(View.VISIBLE);                 // 显示表头
                                mCursor = queryMemento(db, subStr, bodyStr, dateStr); // 调用查询方法
                                SimpleCursorAdapter resultAdapter = new SimpleCursorAdapter(
                                        MainActivity.this,
                                        R.layout.result,
                                        mCursor,
                                        new String[]{"_id", "subject", "body", "date"},
                                        new int[]{R.id.list_num, R.id.list_subject, R.id.list_body, R.id.list_date}
                                );
                                mResult.setAdapter(resultAdapter);  // 设置下拉列表内容
                                break;

                default:break;
            }
        }
    }

    // 插入数据库
    private void addMemento(SQLiteDatabase db, String subStr, String bodyStr, String dateStr) {

        db.execSQL("insert into myTable_db values(null,?,?,?)", new String[]{subStr, bodyStr, dateStr});

        this.mSubject.setText("");  // 添加数据后，将所有编辑框清空
        this.mBody.setText("");
        this.mDate.setText("");
    }

    // 查询数据库
    private Cursor queryMemento(SQLiteDatabase db, String subStr, String bodyStr, String dateStr) {

        Cursor cursor = db.rawQuery("select * from myTable_db where subject like ? and body like ? and date like ?",
                                    new String[]{"%"+subStr+"%","%"+bodyStr+"%","%"+dateStr+"%"});
        return cursor;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();          //　没有这个退出就崩溃。
        if (myDBHelper != null) {
            myDBHelper.close();
        }
    }
}
