package com.example.life_and_calorie.userpage;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.life_and_calorie.R;
import com.example.life_and_calorie.main.Food;
import com.example.life_and_calorie.main.FoodAdapter;
import com.example.life_and_calorie.main.MySQLiteOpenHelper;

import java.util.ArrayList;

public class BookMarkFragment extends Fragment {
    ArrayList<Food> user_al_food;
    ArrayAdapter<Food> user_aa_food;
    ListView user_bookmark_list;
    Cursor mCursor;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.activity_book_mark_fragment, container, false);
        //food_bookmark.db 파일이 있으면 오픈, 없으면 생성 후 오픈
        MySQLiteOpenHelper dbHelper = new MySQLiteOpenHelper(getContext(), "LifeAndCalorie.db", null, 1);
        //읽고 쓰기가 가능한 SQLiteDatabase 객체를 반환한다.
        SQLiteDatabase mdb = dbHelper.getWritableDatabase();
        mCursor = mdb.rawQuery("SELECT * FROM Bookmark", null);
        user_al_food = new ArrayList<Food>();
        user_aa_food = new FoodAdapter(getContext(), R.layout.row, R.id.tv_foodname, user_al_food);
        user_bookmark_list = (ListView)view.findViewById(R.id.user_bookmark_list);
        user_bookmark_list.setAdapter(user_aa_food);
        while(mCursor.moveToNext()){
            String foodname = mCursor.getString(0);
            String foodkcal = mCursor.getString(1);

            user_al_food.add(new Food(foodname, foodkcal, true));
        }
        user_aa_food.notifyDataSetChanged();





        return view;
    }
}