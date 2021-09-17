package com.example.life_and_calorie.userpage;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.life_and_calorie.R;


public class CalorieAndWeightSetDialog extends Dialog {
    private Context context;
    EditText et_calorie, et_weight, et_name;
    private TextView  setting_dialog_tv_negative, setting_dialog_tv_positive, tv_root_name,tv_root_calorie_and_weight;

    //생성자
    public CalorieAndWeightSetDialog(@NonNull Context context, TextView name, TextView calorie_and_weight) {
        super(context);
        this.context = context;
        this.tv_root_name = name;
        this.tv_root_calorie_and_weight = calorie_and_weight;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_weight_set);

        et_calorie = (EditText)findViewById(R.id.et_calorie_goal_value);
        et_weight = (EditText)findViewById(R.id.et_weight_goal_value);
        et_name = (EditText)findViewById(R.id.et_name);

        //Dialog 저장버튼
        setting_dialog_tv_positive = findViewById(R.id.setting_dialog_tv_positive);
        //Dialog 취소버튼
        setting_dialog_tv_negative = findViewById(R.id.setting_dialog_tv_negative);
        //저장하기 버튼 클릭 시
        setting_dialog_tv_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = context.getSharedPreferences("sharedpreferences",MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("nickname", et_name.getText().toString());
                editor.putFloat("weight", Float.parseFloat(et_weight.getText().toString()));
                editor.putInt("calorie", Integer.parseInt(et_calorie.getText().toString()));
                editor.commit();
                //Dialog 종료
                dismiss();
                tv_root_name.setText(et_name.getText());
                tv_root_calorie_and_weight.setText(et_weight.getText().toString() + "kg  /  " + et_calorie.getText().toString() + "kcal");

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