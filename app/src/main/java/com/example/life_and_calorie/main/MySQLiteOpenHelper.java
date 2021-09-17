package com.example.life_and_calorie.main;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    public MySQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //사용중인 DB가 없을 때 호출
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE if not exists Bookmark (bookmark_food TEXT, bookmark_kcal TEXT);");
        sqLiteDatabase.execSQL("CREATE TABLE if not exists Weight (date TEXT, weight TEXT);");
        String sql = "CREATE TABLE if not exists USERDATA (_id integer primary key autoincrement,FoodName text, Calorie text, DATE text);";
        sqLiteDatabase.execSQL(sql);
    }
    //onUpdate()는 사용중인 코드의 버전이 바뀐 경우 호출
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
