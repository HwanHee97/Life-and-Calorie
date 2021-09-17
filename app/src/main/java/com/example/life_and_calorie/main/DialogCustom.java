package com.example.life_and_calorie.main;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.life_and_calorie.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DialogCustom extends Dialog {
    private Context context;
    private TextView dialog_tv_title, dialog_tv_negative, dialog_tv_positive;
    private int food_count;
    TextView dialog_tv_foodname2;
    TextView dialog_tv_kcal2;
    TextView dialog_tv_date2;
    ImageView dialog_iv_date;
    EditText dialog_et_count2;
    String calendar_date;
    Food food_info;
    MySQLiteOpenHelper dbHelper;
    SQLiteDatabase db;

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
        dbHelper = new MySQLiteOpenHelper(getContext(), "LifeAndCalorie.db", null, 1);
        db = dbHelper.getWritableDatabase();
        //Dialog 제목
        dialog_tv_title = findViewById(R.id.dialog_tv_title);
        //Dialog 음식명
        dialog_tv_foodname2 = findViewById(R.id.dialog_tv_foodname2);
        //Dialog 칼로리
        dialog_tv_kcal2 = findViewById(R.id.dialog_tv_kcal2);
        //Dialog 일정
        dialog_tv_date2 = findViewById(R.id.dialog_tv_date2);
        dialog_iv_date = findViewById(R.id.dialog_iv_date);
        Calendar calendar = Calendar.getInstance();
        int cYear = calendar.get(Calendar.YEAR);
        int cMonth = calendar.get(Calendar.MONTH);
        int cDay = calendar.get(Calendar.DAY_OF_MONTH);
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
        dialog_tv_date2.setText(calendar_date);

        //현재 날짜가 아닌 일정을 변경하였을 시
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String _month = "";
                String _dayOfMonth = "";
                _month = String.format("%02d", (month + 1));
                _dayOfMonth = String.format("%02d", dayOfMonth);
                calendar_date = year + "-" + _month + "-" + _dayOfMonth;
                dialog_tv_date2.setText(year + "-" + _month + "-" + _dayOfMonth);
            }
        }, cYear, cMonth, cDay);
        dialog_iv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog_iv_date.isClickable()) {
                    datePickerDialog.show();
                }
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
                if (dialog_et_count2.getText().toString().equals("")) {
                    food_count = 1;
                    foodAdd();
                }
                else if(dialog_et_count2.getText().toString().equals("0")){
                    Toast.makeText(getContext(), "수량을 입력하세요", Toast.LENGTH_SHORT).show();
                    dialog_et_count2.setText("");
                    dialog_et_count2.requestFocus();
                }
                else {
                    food_count = Integer.parseInt(dialog_et_count2.getText().toString());
                    foodAdd();
                }
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
    //음식 추가하기
    public void foodAdd (){
        //음식 정보에서의 이름 가져와 food에 저장
        String food = food_info.getName();
        //먹은 칼로리 계산(음식의 칼로리 * 수량)한 후 kcal에 저장
        String kcal = (int) (Double.parseDouble(food_info.getKcal()) * food_count) + "";
        //먹은 날짜
        String date = dialog_tv_date2.getText().toString();
        db.execSQL("INSERT INTO USERDATA ('FoodName', 'Calorie', 'DATE') VALUES ('" + food + "','" + kcal + "','" + calendar_date + "');");
        //Dialog 종료
        dismiss();
        Toast.makeText(getContext(), "추가되었습니다", Toast.LENGTH_SHORT).show();
    }
}
