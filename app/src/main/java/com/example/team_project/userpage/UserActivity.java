package com.example.team_project.userpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.team_project.R;

public class UserActivity extends AppCompatActivity {
    private final int frag_Bookmark = 1;
    private final int frag_Calorie = 2;
    private final int frag_Weight = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        SharedPreferences pref = getSharedPreferences("sharedpreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        TextView tv_id = (TextView) findViewById(R.id.tv_id);
        TextView tv_calorie = (TextView) findViewById(R.id.tv_calorie);

        tv_id.setText("사용자 이름 : " + pref.getString("nickname",""));
        tv_calorie.setText("설정 몸무게 : " + pref.getInt( "weight",0) + ", 설정 칼로리 : " + pref.getInt("calorie",0));

        findViewById(R.id.btn_bookmark).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentView(frag_Bookmark);
            }
        });

        findViewById(R.id.btn_calorie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentView(frag_Calorie);
            }
        });

        findViewById(R.id.btn_weight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentView(frag_Weight);
            }
        });

        FragmentView(frag_Bookmark);
    }

    private void FragmentView(int fragment){
        //FragmentTransactiom를 이용해 프래그먼트를 사용합니다.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (fragment){
            case 1:
                // 첫번 째 프래그먼트 호출
                BookMarkFragment frag_bookmark = new BookMarkFragment();
                transaction.replace(R.id.main_frame, frag_bookmark);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
                break;

            case 2:
                // 두번 째 프래그먼트 호출
                CalorieFragment frag_calorie = new CalorieFragment();
                transaction.replace(R.id.main_frame, frag_calorie);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
                break;

            case 3:
                // 세번 째 프래그먼트 호출
                WeightFragment frag_weight = new WeightFragment();
                transaction.replace(R.id.main_frame, frag_weight);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
                break;
        }
    }
}