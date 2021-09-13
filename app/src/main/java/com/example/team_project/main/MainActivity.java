package com.example.team_project.main;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.team_project.community.BoardFragment;
import com.example.team_project.R;
import com.example.team_project.userpage.UserActivity;
import com.example.team_project.calendar_fragment.CalendarFragment;
import com.example.team_project.main.GPSFragment;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
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
    GPSFragment GPSfragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Mainfragment = new MainFragment();
        Calendarfragment = new CalendarFragment();
        Boardfragment = new BoardFragment();
        GPSfragment = new GPSFragment();
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
                getSupportFragmentManager().beginTransaction().replace(R.id.container, Boardfragment).commit();/*프래그먼트 매니저가 프래그먼트를 담당한다!*/
            }
        });

        //GPS 클릭 시
        TextView tv_gps = findViewById(R.id.tv_gps);
        tv_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, GPSfragment).commit();/*프래그먼트 매니저가 프래그먼트를 담당한다!*/
            }
        });
    }
}