package com.example.life_and_calorie.main;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.life_and_calorie.R;
import com.example.life_and_calorie.retrofit.FoodRetrofitInterface;
import com.example.life_and_calorie.retrofit.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodAdapter extends ArrayAdapter<Food> {
    ArrayList<Food> foods;
    LayoutInflater inflater;
    //food_bookmark.db 파일이 있으면 오픈, 없으면 생성 후 오픈
    MySQLiteOpenHelper dbHelper = new MySQLiteOpenHelper(getContext(), "LifeAndCalorie.db", null, 1);
    //읽고 쓰기가 가능한 SQLiteDatabase 객체를 반환한다.
    SQLiteDatabase mdb = dbHelper.getWritableDatabase();
    //커서
    Cursor mCursor;
    FoodRetrofitInterface server = new RetrofitClient().retrofit.create(FoodRetrofitInterface.class);

    public FoodAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull ArrayList<Food> objects) {
        super(context, resource, textViewResourceId, objects);
        foods = new ArrayList<Food>();
        foods = objects;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.row, null);
        }
        CheckBox cb_bookmark = (CheckBox) view.findViewById(R.id.cb_bookmark);
        //즐겨찾기 되어있는 음식 즐겨찾기 표시
        cb_bookmark.setChecked(foods.get(position).checked);

        //즐겨찾기 클릭 시
        cb_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor mCursor;
                boolean newState = !foods.get(position).isChecked();
                foods.get(position).checked = newState;
                //DB에 저장할 음식이름
                String db_foodname = foods.get(position).getName();
                //DB에 저장할 음식칼로리
                String db_foodkcal = foods.get(position).getKcal();
                //즐겨찾기 설정 시
                if (newState == true) {
                    //삽입
                    mdb.execSQL("INSERT INTO Bookmark VALUES('" + db_foodname + "', +" + db_foodkcal + ");");
                    BOOKMARK();
                } else {
                    //삭제
                    mdb.execSQL("DELETE FROM Bookmark WHERE bookmark_food= '" + db_foodname + "';");
                    BOOKMARK();
                }
            }
        });

        TextView tv_foodname = (TextView) view.findViewById(R.id.tv_foodname);
        tv_foodname.setText(foods.get(position).getName());
        TextView tv_foodcalorie = (TextView) view.findViewById(R.id.tv_foodcalorie);
        tv_foodcalorie.setText(foods.get(position).getKcal() + "kcal");
        ImageButton btn_report = (ImageButton) view.findViewById(R.id.btn_report);

        //신고버튼 클릭 시
        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DB에 저장할 음식이름
                String report_food = foods.get(position).getName();
                //DB에 저장할 음식칼로리
                String report_kcal = foods.get(position).getKcal();
                reportFood(report_food, report_kcal);
                Toast.makeText(getContext(), "신고되었습니다", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    //즐겨찾기 조회
    public void BOOKMARK() {
        String str = "\n즐겨찾기 조회\n";
        mCursor = mdb.rawQuery("SELECT * FROM Bookmark", null);
        while (mCursor.moveToNext()) {
            String foodname = mCursor.getString(0);
            String foodkcal = mCursor.getString(1);
            str += (foodname + " - " + foodkcal + "\n");
        }
    }

    public void reportFood(String report_food, String report_kcal) {
        Call<String> call = server.reportFood(report_food, report_kcal);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    //makeArraryList(response.body().toString());
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
}
