package com.example.life_and_calorie.calendar_fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE if not exists USERDATA (_id integer primary key autoincrement,FoodName text, Calorie text, DATE text, WEIGHT text);";

        db.execSQL(sql);

    }
    public void insert(SQLiteDatabase db){
        String sql = "INSERT INTO USERDATA ('FoodName','Calorie','DATE','WEIGHT')VALUES('불고기','340','2021-09-07','63.7')";
        db.execSQL(sql);
        Cursor c = db.rawQuery("SELECT * FROM USERDATA;",null);
        while(c.moveToNext()){
            Log.i("suyong", "txt: " + c.getString(1) + c.getString(2) + c.getString(3)+ c.getString(4));
        }
    }
    public String select(SQLiteDatabase db){
        Cursor c = db.rawQuery("SELECT * FROM USERDATA;",null);
        return "";
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "DROP TABLE if exists USERDATA";
        db.execSQL(sql);
        onCreate(db);
    }
}
