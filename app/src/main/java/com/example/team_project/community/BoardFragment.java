package com.example.team_project.community;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.team_project.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardFragment extends Fragment implements View.OnClickListener {
    final static int REQUEST_CODE_ADD = 1;
    RecyclerView community_RecyclerView = null;
    RecyclerAdapter RecyclerAdapter = null;
    ArrayList<RecyclerItem> community_List = new ArrayList<RecyclerItem>();
    String list_index;
    ProgressBar progressBar;
    Button btn_addPage;
    boolean mLockListView = false;  // 데이터 불러올때 중복안되게 하기위한 변수
    ViewGroup rootView;
    CommunityRetrofitInterface server = new RetrofitClient().retrofit.create(CommunityRetrofitInterface.class);


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //프래그먼트 메뉴를 인플레이트해주고 컨테이너에 붙여달라는 뜻임
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_board, container, false);
        init();
        list_index = "0";
        showCommunityList(list_index);
        return rootView;
    }
    public void init() {
        community_RecyclerView = rootView.findViewById(R.id.Recycler);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressbar);
        btn_addPage = (Button) rootView.findViewById(R.id.btn_addPage);
        btn_addPage.setOnClickListener(this);
        progressBar.setVisibility(View.GONE);
        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        RecyclerAdapter = new RecyclerAdapter(community_List);
        community_RecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), 1));//구분선 넣기
        community_RecyclerView.setAdapter(RecyclerAdapter);//어댑터 설정
        community_RecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));//레이아웃 설정
        community_RecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!community_RecyclerView.canScrollVertically(-1)) {
                    Log.i("lhh", "Top of list");
                } else if (!community_RecyclerView.canScrollVertically(1) && mLockListView == false) {
                    Log.i("lhh", "End of list");
                    progressBar.setVisibility(View.VISIBLE);
                    mLockListView = true;
                    showCommunityList(list_index);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            RecyclerAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            mLockListView = false;
                        }
                    }, 1000);
                } else {
                    Log.i("lhh", "idle");
                }
            }
        });

    }

    public void showCommunityList(String index) {//커뮤니티 글가져오는 함수
        Call<String> call = server.showCommunity(index);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    //Log.i("lhh", response.body().toString());
                    makeCommunityArraryList(response.body().toString());
                    RecyclerAdapter.notifyDataSetChanged();//변경여부 최신화
                } else {
                    Log.i("lhh", "실패");
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("lhh", "실패" + t.getMessage());
            }
        });
        list_index = Integer.parseInt(index) + 10 + "";
    }

    public void makeCommunityArraryList(String str_json) {
        Log.i("lhh", " <<<<<데이터 json->Arrarylist>>>> ");
        try {
            int count = 0;
            JSONArray jarray = new JSONObject(str_json).getJSONArray("List");//str_json라는 json형식의 문자열을 json객체로 만들고 json배열로 만든다.
            while (jarray != null) {
                JSONObject jsonObject = jarray.getJSONObject(count);
//
                String _ID = jsonObject.getString("_ID");//키값 맞춰서 읽어들임
                String title = jsonObject.getString("TITLE");
                String text = jsonObject.getString("TEXT");
                String date = jsonObject.getString("DATE");
                String writer_ID = jsonObject.getString("WRITER_ID");
                community_List.add(new RecyclerItem(_ID, title, text, date, writer_ID));
                count++;
            }
            //RecyclerAdapter.notifyDataSetChanged();//변경여부 최신화
        } catch (Exception e) {
            Log.e("lhh", e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {//추가하려는 글을 리스트에 추가
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD) {
            if (resultCode == RESULT_OK) {
                RecyclerItem tmp_item = (RecyclerItem) data.getSerializableExtra("community_item");
                community_List.add(tmp_item);
                //레트로 핏을통해 서버로 DB에 저장할 게시글 정보 넘긴다.
                Call<String> call = server.insertCommunity(tmp_item.getTitle(), tmp_item.getText(), tmp_item.getWriter_ID());
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            Log.i("lhh", response.body().toString());
                            RecyclerAdapter.notifyDataSetChanged();//변경여부 최신화
                        } else {
                            Log.i("lhh", "실패");
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.i("lhh", "실패" + t.getMessage());
                    }
                });
                RecyclerAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onClick(View view) {//리사이클러 뷰(글) 추가하기 위한 페이지로 넘어감
        Intent intent = new Intent(getContext(), AddPage.class);
        startActivityForResult(intent, REQUEST_CODE_ADD);
    }
}
