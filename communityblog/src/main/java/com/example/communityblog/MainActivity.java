package com.example.communityblog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    final static int REQUEST_CODE_ADD = 1;
    RecyclerView community_RecyclerView = null;
    RecyclerAdapter RecyclerAdapter = null;
    ArrayList<RecyclerItem> community_List = new ArrayList<RecyclerItem>();
    CONN_SERVER_COMMUNITY task;
    int list_index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        community_RecyclerView = findViewById(R.id.Recycler);

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        RecyclerAdapter = new RecyclerAdapter(community_List);
        community_RecyclerView.addItemDecoration(new DividerItemDecoration(this, 1));//구분선 넣기
        community_RecyclerView.setAdapter(RecyclerAdapter);//어댑터 설정
        community_RecyclerView.setLayoutManager(new LinearLayoutManager(this));//레이아웃 설정
        //테스트 위한 코드 리스트 추가하고 확인
//        addItem("1", "aaa","user1");
//        addItem("2", "bbb","user2");
//        addItem("3", "ccc","user3");
//        addItem("4", "ddd","user4");
//        addItem("5", "eee","user5");
//        RecyclerAdapter.notifyDataSetChanged();//변경여부 최신화
        //list_index = 10;
        Show_CommunityList();
    }
//    private void addItem(String Title, String Text,String writer_ID) {//테스트 위한 추가 함수
//        RecyclerItem item = new RecyclerItem();
//
//        item.setTitle(Title);
//        item.setText(Text);
//        item.setWriter_ID(writer_ID);
//        community_List.add(item);
//    }


    public void Show_CommunityList() {//디비에서 인덱스-10 으로 10개씩만 가져올것임
        task = new CONN_SERVER_COMMUNITY();
        try {
            make_communityArraryList(task.execute("select").get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void make_communityArraryList(String str_json) {

        Log.i("lhh", " <<<<<데이터 json->Arrarylist>>>> ");
        try {
            int count = 0;
            JSONArray jarray = new JSONObject(str_json).getJSONArray("List");//result라는 json형식의 문자열을 json객체로 만들고 json배열로 만든다.
            while (jarray != null) {
                JSONObject jsonObject = jarray.getJSONObject(count);
//
                String _ID = jsonObject.getString("_ID");//키값 맞춰서 읽어들임
                String title = jsonObject.getString("TITLE");//키값 맞춰서 읽어들임
                String text = jsonObject.getString("TEXT");
                String date = jsonObject.getString("DATE");
                String writer_ID = jsonObject.getString("writer_ID");
                community_List.add(new RecyclerItem(_ID, title, text, date, writer_ID));
                count++;
            }
            RecyclerAdapter.notifyDataSetChanged();//변경여부 최신화
        } catch (Exception e) {
            Log.e("lhh", e.getMessage());
        }
    }


    public void add(View v) {//추가하기 위한 페이지로 넘어감
        Intent intent = new Intent(MainActivity.this, AddPage.class);
        startActivityForResult(intent, REQUEST_CODE_ADD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {//추가하려는 글을 리스트에 추가
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD) {
            if (resultCode == RESULT_OK) {
                RecyclerItem tmp_item = (RecyclerItem) data.getSerializableExtra("community_item");
                community_List.add(tmp_item);
                RecyclerAdapter.notifyDataSetChanged();
            }
        }
    }


}