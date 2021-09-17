package com.example.life_and_calorie.community;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.life_and_calorie.R;
import com.example.life_and_calorie.community.itemclass.RecyclerItem;

public class AddPage extends AppCompatActivity {
    TextView input_title;
    TextView input_text;
    String user_nickname = "";//전역변수인 사용자 ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpage);
        init();
    }

    public void init() {
        SharedPreferences pref = getSharedPreferences("sharedpreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        user_nickname = pref.getString("nickname", "Unknown");
        input_title = (TextView) findViewById(R.id.community_AddTitle_et);
        input_text = (TextView) findViewById(R.id.community_AddText_et);
    }

    public void Write(View v) {
        RecyclerItem item = new RecyclerItem(input_title.getText().toString(), input_text.getText().toString(), user_nickname);

        Intent intent = new Intent();
        intent.putExtra("community_item", item);

        setResult(RESULT_OK, intent);
        finish();
    }
}