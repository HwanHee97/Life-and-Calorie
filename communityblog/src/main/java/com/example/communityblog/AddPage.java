package com.example.communityblog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPage extends AppCompatActivity {
TextView input_title;
TextView input_text;
String writer_ID="사용자";//전역변수인 사용자 ID
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpage);
        input_title=(TextView)findViewById(R.id.community_AddTitle_et);
        input_text=(TextView) findViewById(R.id.community_AddText_et);


    }
    public void Write(View v){

        RecyclerItem item=new RecyclerItem(input_title.getText().toString(),input_text.getText().toString(), writer_ID);

        Intent intent = new Intent();
        intent.putExtra("community_item", item);

        setResult(RESULT_OK,intent);
        finish();
    }
}