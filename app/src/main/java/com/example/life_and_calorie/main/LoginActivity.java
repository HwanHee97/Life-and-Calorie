package com.example.life_and_calorie.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.life_and_calorie.R;

public class LoginActivity extends AppCompatActivity {
    //닉네임 입력
    EditText et_nickname;
    //몸무게 입력
    EditText et_weight;
    //칼로리 입력
    EditText et_calorie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_nickname = (EditText) findViewById(R.id.et_nickname);
        et_weight = (EditText) findViewById(R.id.et_weight);
        et_calorie = (EditText) findViewById(R.id.et_calorie);
    }

    public void onClickLogin(View view) {
        //닉네임 입력 공란일 경우
        if (et_nickname.getText().toString().length() == 0) {
            et_nickname.setText("");
            et_weight.setText("");
            et_nickname.requestFocus();
        }
        //몸무게 입력 공란일 경우
        else if (et_weight.getText().toString().length() == 0) {
            et_weight.setText("");
            et_weight.requestFocus();
        }
        //칼로리 입력 공란일 경우
        else if (et_calorie.getText().toString().length() == 0) {
            et_calorie.setText("");
            et_calorie.requestFocus();
        }
        //로그인 성공
        else {
            //입력한 닉네임 가져오기
            String nickname = et_nickname.getText().toString();
            //입력한 몸무게 가져오기
            float weight = Float.parseFloat(et_weight.getText().toString());
            //입력한 칼로리 가져오기
            int calorie = Integer.parseInt(et_calorie.getText().toString());
            WriteName(nickname, weight, calorie);
            //MainActivity 실행 후 LoginActivity 종료
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //닉네임 및 몸무게 저장
    void WriteName(String nickname, float weight, int calorie) {
        SharedPreferences pref = getSharedPreferences("sharedpreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("nickname", nickname);
        editor.putFloat("weight", weight);
        editor.putInt("calorie", calorie);
        editor.commit();
    }
}