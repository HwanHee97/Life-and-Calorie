package com.example.life_and_calorie.calendar_fragment;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.life_and_calorie.R;


public class CustomDialog extends Dialog {
    private Context context;
    private String date;
    private TextView  setting_dialog_tv_negative, setting_dialog_tv_positive;
    DBHelper dbHelper;
    SQLiteDatabase db;
    EditText setting_dialog_et_current_weight;
    TextView tv_weight;
    //생성자
    public CustomDialog(@NonNull Context context, String date, TextView tv_weight) {
        super(context);
        this.date = date;
        this.context = context;
        this.tv_weight = tv_weight;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_dialog);
        dbHelper = new DBHelper(getContext(), "LifeAndCalorie.db", null, 1);
        db = dbHelper.getWritableDatabase();

        //Dialog 현재 몸무게
        setting_dialog_et_current_weight = findViewById(R.id.setting_dialog_et_current_weight);
        //Dialog 저장버튼
        setting_dialog_tv_positive = findViewById(R.id.setting_dialog_tv_positive);
        //Dialog 취소버튼
        setting_dialog_tv_negative = findViewById(R.id.setting_dialog_tv_negative);

        //저장하기 버튼 클릭 시
        setting_dialog_tv_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.execSQL("INSERT INTO WEIGHT (date,weight) VALUES ('"+date+"','"+ setting_dialog_et_current_weight.getText().toString() +"');");
                tv_weight.setText(setting_dialog_et_current_weight.getText().toString());
                //Dialog 종료
                dismiss();
            }
        });
        //취소 버튼 클릭 시
        setting_dialog_tv_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Dialog 종료
                dismiss();
            }
        });
    }
}