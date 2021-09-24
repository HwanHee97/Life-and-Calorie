package com.example.life_and_calorie.community;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.life_and_calorie.R;
import com.example.life_and_calorie.community.adapter.CommentAdapter;
import com.example.life_and_calorie.community.itemclass.Community_Comment_Item;
import com.example.life_and_calorie.community.itemclass.RecyclerItem;
import com.example.life_and_calorie.retrofit.CommunityRetrofitInterface;
import com.example.life_and_calorie.retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowWriting extends AppCompatActivity {
    TextView title;
    TextView date;
    TextView writerID;
    TextView text;

    ListView comment_listview;
    ArrayList<Community_Comment_Item> comment_ArrayList;
    ArrayAdapter<Community_Comment_Item> comment_adapter;
    String _ID = "";
    String user_nickname="";
    CommunityRetrofitInterface server = new RetrofitClient().retrofit.create(CommunityRetrofitInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_writing);

        init();
        showCommunityCommentList();//댓글 리스트 불러와서 출력한다.
    }

    public void init(){
        SharedPreferences pref = getSharedPreferences("sharedpreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        user_nickname=pref.getString("nickname", "Unknown") ;

        title = (TextView) findViewById(R.id.show_title_tv);
        date = (TextView) findViewById(R.id.show_date_tv);
        writerID = (TextView) findViewById(R.id.show_writerID_tv);
        text = (TextView) findViewById(R.id.show_text_tv);
        comment_listview = (ListView) findViewById(R.id.community_comment_List);

        comment_ArrayList = new ArrayList<Community_Comment_Item>();
        comment_adapter = new CommentAdapter(this, R.layout.comment_list_item_layout, R.id.commentItem_text, comment_ArrayList);

        comment_listview.setAdapter(comment_adapter);//어뎁터 연결

        Intent intent = getIntent();
        RecyclerItem item = (RecyclerItem) intent.getSerializableExtra("community_item");
        _ID = item.get_ID();
        title.setText("(" + item.get_ID() + ")" + item.getTitle());
        date.setText(item.getDate());
        writerID.setText(item.getWriter_ID());
        text.setText(item.getText());

    }

    public void showCommunityCommentList() {//커뮤니티 글가져오는 함수
        Call<String> call = server.commentShow(_ID);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    makeCommunityCommentArraryList(response.body().toString());
                    comment_adapter.notifyDataSetChanged();
                } else {
                    Log.i("lhh", "실패");
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("lhh", "실패" + t.getMessage());
            }
        });
    }

    public void makeCommunityCommentArraryList(String str_json) {
        Log.i("lhh", " <<<<<게시판댓글 데이터 json->Arrarylist>>>> ");
        try {
            int count = 0;
            JSONArray jarray = new JSONObject(str_json).getJSONArray("List");//str_json라는 json형식의 문자열을 json객체로 만들고 json배열로 만든다.
            while (jarray != null) {
                JSONObject jsonObject = jarray.getJSONObject(count);

                String text = jsonObject.getString("TEXT");
                String date = jsonObject.getString("DATE");
                String writer_ID = jsonObject.getString("WRITER_ID");
                comment_ArrayList.add(new Community_Comment_Item(text, date, writer_ID));
                count++;
            }
            comment_adapter.notifyDataSetChanged();//변경여부 최신화
        } catch (Exception e) {
            Log.e("lhh", e.getMessage());
        }
    }

    public void addComment(View v) {
        EditText comment_et = (EditText) findViewById(R.id.add_comment_et);
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String today = format.format(date);

        comment_ArrayList.add(new Community_Comment_Item(comment_et.getText().toString(), today, user_nickname));
        //서버로 댓글,낳짜, 글쓴이 넘겨서 디비에 추가하는 함수
        Call<String> call = server.commentInsert(_ID, comment_et.getText().toString(), today, user_nickname);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    comment_adapter.notifyDataSetChanged();
                } else {
                    Log.i("lhh", "실패");
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("lhh", "실패" + t.getMessage());
            }
        });
        comment_et.setText("");
    }
}


