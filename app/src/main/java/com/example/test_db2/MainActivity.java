package com.example.test_db2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    // 마지막으로 뒤로 가기 버튼을 눌렀던 시간 저장
    private long backKeyPressedTime = 0;
    // 첫 번째 뒤로 가기 버튼을 누를 때 표시
    private Toast toast;
    TextView tv;
    ArrayList<Food> foodList;
    CONN_SERVER task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv);
        foodList = new ArrayList<Food>();

    }

    public void insertdata(View v) {//버튼 이벤트
        insertFood("insert", "바바바", "3000", "삼양3");
    }

    public void getdata(View v) {//버튼 이벤트
        tv.setText("데이터 가져오는중");
        selectFood("select", "바바바");
    }

    public void selectFood(String sql_action, String food) {
        try {
            task = new CONN_SERVER();
            //tv.setText(task.execute("insert","바바바","3000","농심").get());
            makeArraryList(task.execute(sql_action, food).get());
            //tv.setText(task.execute(sql_action, food).get());//doInBackground결과를 .get로 가져온다.
            //new check_date().execute("select");이렇게도 호출가능
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void makeArraryList(String str_json) {
        tv.setText(str_json);
        Log.i("lhh", " <<<<<onPostExecute>>>> ");
        try {
            int count = 0;
            JSONArray jarray = new JSONObject(str_json).getJSONArray("List");//result라는 json형식의 문자열을 json객체로 만들고 json배열로 만든다.
            while (jarray != null) {
                JSONObject jsonObject = jarray.getJSONObject(count);
//
                String FOOD = jsonObject.getString("FOOD");//키값 맞춰서 읽어들임
                String CALORIE = jsonObject.getString("CALORIE");
                String SOURCE = jsonObject.getString("SOURCE");

                foodList.add(new Food(FOOD, Integer.parseInt(CALORIE), SOURCE));
                //Log.i("lhh", FOOD + "/" + CALORIE + "/" + SOURCE);
                count++;

            }
        } catch (Exception e) {
            Log.e("lhh", e.getMessage());
        }
    }

    public void insertFood(String sql_action, String food, String calorie, String source) {
        try {
            task = new CONN_SERVER();
            tv.setText(task.execute(sql_action, food, calorie, source).get());//성공메시지 출력
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // 기존 뒤로 가기 버튼의 기능을 막기 위해 주석 처리 또는 삭제

        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지났으면 Toast 출력
        // 2500 milliseconds = 2.5 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지나지 않았으면 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finish();
            toast.cancel();
            toast = Toast.makeText(this, "이용해 주셔서 감사합니다.", Toast.LENGTH_LONG);
            toast.show();
        }
    }

}//메인액티비티 클래스 끝