package com.example.life_and_calorie.userpage;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.life_and_calorie.R;

public class UserActivity extends AppCompatActivity {
    private final int frag_Bookmark = 1;
    private final int frag_Calorie = 2;
    private final int frag_Weight = 3;
    TextView tv_id, tv_calorie;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        pref = getSharedPreferences("sharedpreferences", MODE_PRIVATE);
        editor = pref.edit();
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);

        tv_id = (TextView) findViewById(R.id.tv_id);
        tv_calorie = (TextView) findViewById(R.id.tv_calorie);

        tv_id.setText("" + pref.getString("nickname", ""));
        tv_calorie.setText(pref.getFloat("weight", 0.0F) + "kg  /  " + pref.getInt("calorie", 0) + "kcal");
        rl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //CustomDialog 객체 생성
                CalorieAndWeightSetDialog dialogCustom = new CalorieAndWeightSetDialog(UserActivity.this,tv_id,tv_calorie);
                //Dialog 밖에 터치 했을 때 Dialog가 꺼짐
                dialogCustom.setCanceledOnTouchOutside(true);
                //Dialog 취소 가능(back버튼)
                dialogCustom.setCancelable(true);
                //CustomDialog의 크기설정
                dialogCustom.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogCustom.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                //DialogCustom show
                dialogCustom.show();

                return false;
            }
        });
        findViewById(R.id.user_iv_bookmark).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentView(frag_Bookmark);
            }
        });

        findViewById(R.id.user_iv_calorie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentView(frag_Calorie);
            }
        });

        findViewById(R.id.user_iv_weight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentView(frag_Weight);
            }
        });

        FragmentView(frag_Bookmark);
    }

    private void FragmentView(int fragment) {
        //FragmentTransactiom를 이용해 프래그먼트를 사용합니다.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (fragment) {
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

    @Override
    protected void onResume() {
        Log.i("suyong","resume");
        tv_id.setText("" + pref.getString("nickname", ""));
        tv_calorie.setText(pref.getFloat("weight", 0.0F) + "kg  /  " + pref.getInt("calorie", 0) + "kcal");
        super.onResume();
    }
}