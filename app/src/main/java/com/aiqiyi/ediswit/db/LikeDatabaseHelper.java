package com.aiqiyi.ediswit.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by tyr on 2017/6/8.
 */
public class LikeDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_LIKE = "create table like("
            +"id text primary key,"
            +"short_title text,"
            +"title text,"
            +"img text)";

    private Context mContext;
    public LikeDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LIKE);
        Toast.makeText(mContext,"Database Succeed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
