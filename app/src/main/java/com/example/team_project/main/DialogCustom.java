package com.example.team_project.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.example.team_project.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DialogCustom extends Dialog {
    private Context context;
    private TextView dialog_tv_title, dialog_tv_negative, dialog_tv_positive;
    TextView dialog_tv_foodname2;
    TextView dialog_tv_kcal2;
    CalendarView dialog_calendar;
    EditText dialog_et_count2;
    Food food_info;
    String calendar_date;

    //생성자
    public DialogCustom(@NonNull Context context, Food food_info) {
        super(context);
        this.context = context;
        this.food_info = food_info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        //Dialog 제목
        dialog_tv_title = findViewById(R.id.dialog_tv_title);
        //Dialog 음식명
        dialog_tv_foodname2 = findViewById(R.id.dialog_tv_foodname2);
        //Dialog 칼로리
        dialog_tv_kcal2 = findViewById(R.id.dialog_tv_kcal2);
        //Dialog 일정
        dialog_calendar = findViewById(R.id.dialog_calendar);
        //Dialog 수량
        dialog_et_count2 = findViewById(R.id.dialog_et_count2);
        //Dialog 저장버튼
        dialog_tv_positive = findViewById(R.id.dialog_tv_positive);
        //Dialog 취소버튼
        dialog_tv_negative = findViewById(R.id.dialog_tv_negative);

        //현재 날짜 불러오기
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = sdf.format(date);
        //날짜를 선택하지않으면 자동으로 현재 날짜로 설정
        calendar_date = getTime;

        //현재 날짜가 아닌 일정을 변경하였을 시
        dialog_calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                //선택된 날짜
                calendar_date = year+"-"+month+"-"+dayOfMonth;
            }
        });
        //클릭한 ListView의 음식의 이름 표기
        dialog_tv_foodname2.setText(food_info.getName());
        //클릭한 ListView의 음식의 칼로리 표기
        dialog_tv_kcal2.setText(food_info.getKcal());

        //저장하기 버튼 클릭 시
        dialog_tv_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //음식 정보에서의 이름 가져와 food에 저장
                String food = food_info.getName();
                //먹은 칼로리 계산(음식의 칼로리 * 수량)한 후 kcal에 저장
                String kcal = Double.toString(Double.parseDouble(food_info.getKcal()) * Integer.parseInt(dialog_et_count2.getText().toString()));
                //Dialog 종료
                dismiss();
            }
        });
        //취소 버튼 클릭 시
        dialog_tv_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Dialog 종료
                dismiss();
            }
        });
    }
}
