package com.example.life_and_calorie.main;


import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.life_and_calorie.R;
import com.example.life_and_calorie.WidgetProvider;
import com.example.life_and_calorie.calendar_fragment.CalendarFragment;
import com.example.life_and_calorie.calendar_fragment.DBHelper;
import com.example.life_and_calorie.community.BoardFragment;
import com.example.life_and_calorie.userpage.UserActivity;
import com.prolificinteractive.materialcalendarview.CalendarDay;

public class MainActivity extends AppCompatActivity  {
    //프래그먼트는  xml레이아웃 파일 하나랑 자바소스 파일 하나로 정의할 수 있다.
    //이게 하나의 뷰처럼 쓸 수 있는데 뷰하고 약간 다른특성들이 있다.
    //엑티비티를 본떠 만들었기 떄문에 프래그먼트 매니저가 소스코드에서 담당한다.
    //메인 프래그먼트
    MainFragment Mainfragment;
    //달력 프래그먼트
    CalendarFragment Calendarfragment;
    //게시판 프래그먼트
    BoardFragment Boardfragment;
    //GPS 프래그먼트
    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createDB();

        Mainfragment = new MainFragment();
        Calendarfragment = new CalendarFragment();
        Boardfragment = new BoardFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, Mainfragment).commit();/*프래그먼트 매니저가 프래그먼트를 담당한다!*/
        //사용자 버튼 클릭 시
        ImageButton btn_user = findViewById(R.id.btn_user);
        btn_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });

        //검색 클릭 시
        ImageView activity_iv_search = findViewById(R.id.activity_iv_search);
        activity_iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, Mainfragment).commit();/*프래그먼트 매니저가 프래그먼트를 담당한다!*/
            }
        });

        //달력 클릭 시
        ImageView activity_iv_calendar = findViewById(R.id.activity_iv_calendar);
        activity_iv_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, Calendarfragment).commit();/*프래그먼트 매니저가 프래그먼트를 담당한다!*/
            }
        });

        //게시판 클릭 시
        ImageView activity_iv_board = findViewById(R.id.activity_iv_board);
        activity_iv_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boardfragment = new BoardFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, Boardfragment).commit();/*프래그먼트 매니저가 프래그먼트를 담당한다!*/
            }
        });
    }

    @Override
    protected void onPause() {
        Log.i("lhh","onpause");
        super.onPause();

        saveWidgetData();
        Intent widgetIntent = new Intent(this, WidgetProvider.class);
        widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE); // 위젯 업데이트 액션
        this.sendBroadcast(widgetIntent); // 브로드캐스팅
    }

    public void createDB(){
        dbHelper = new DBHelper(MainActivity.this, "LifeAndCalorie.db", null, 1);
        db = dbHelper.getWritableDatabase();
        dbHelper.onCreate(db);
    }

    public void saveWidgetData(){
        SharedPreferences pref=getSharedPreferences("sharedpreferences",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        int day = CalendarDay.today().getDay();
        int month = CalendarDay.today().getMonth()+1;
        int year = CalendarDay.today().getYear();
        String str_date = year + "-" + String.format("%02d",month)+ "-" + String.format("%02d",day);
        Log.i("suyong",str_date);
        Cursor c = db.rawQuery("SELECT * FROM USERDATA WHERE DATE ='" + str_date + "';", null);
        int calorie_sum = 0;
        while (c.moveToNext()) {
            calorie_sum += Integer.parseInt(c.getString(2));
        }
        editor.putInt("present", calorie_sum);
        int calorie_goal = pref.getInt("calorie",0);
        editor.putInt("remain",calorie_goal - calorie_sum);
        editor.commit();
    }
}