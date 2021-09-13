package com.example.team_project.main;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    public MySQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //사용중인 DB가 없을 때 호출
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE Bookmark (bookmark_food TEXT, bookmark_kcal TEXT);");
        sqLiteDatabase.execSQL("CREATE TABLE Report (report_food TEXT, report_kcal TEXT);");
        Log.i("minhxxk", "데이터베이스와 테이블이 생성되었습니다.");
    }

    //onUpdate()는 사용중인 코드의 버전이 바뀐 경우 호출
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
