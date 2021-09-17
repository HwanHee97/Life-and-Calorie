package com.example.life_and_calorie.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.life_and_calorie.R;

public class IntroActivity extends AppCompatActivity {
    static final int PROGRESSBAR_START = 1;
    ProgressBar pb;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == PROGRESSBAR_START) {
                if (pb.getProgress() < pb.getMax()) {
                    pb.setProgress(pb.getProgress() + 1);
                    sendEmptyMessageDelayed(PROGRESSBAR_START, 10);
                }
                //ProgressBar가 다 채워졌을 때
                else {
                    SharedPreferences pref = getSharedPreferences("sharedpreferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    //Preference에 저장한 사용자 이름 과 몸무게 출력


                    //Preference에 사용자의 닉네임이 공백이 아니고 몸무게가 0이 아닐 경우
                    if (pref.getString("nickname", "") != "" && pref.getFloat("weight", 0.0F) != 0 && pref.getInt("calorie", 0) != 0) {
                        //MainActivity 실행
                        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        //LoginActivity 실행
                        Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        pb = (ProgressBar) findViewById(R.id.pb);
        pb.setProgress(0);
        handler.sendEmptyMessage(PROGRESSBAR_START);
    }
}