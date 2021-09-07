package com.example.communityblog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ShowWriting extends AppCompatActivity {
TextView title;
TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_writing);
        title=(TextView) findViewById(R.id.show_title);
        text=(TextView) findViewById(R.id.show_text);
        Intent intent = getIntent();
        RecyclerItem item = (RecyclerItem) intent.getSerializableExtra("community_item");
        title.setText(item.getTitle());
        text.setText(item.getText());
    }
}