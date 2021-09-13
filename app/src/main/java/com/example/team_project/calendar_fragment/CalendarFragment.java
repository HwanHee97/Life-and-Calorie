package com.example.team_project.calendar_fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.team_project.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;

public class CalendarFragment extends Fragment {
    MaterialCalendarView materialCalendarView;
    SuccessDayDecorator successDayDecorator;
    ArrayList<SuccessDayDecorator> successDayDecoratorsAL;
    ArrayList<UserData> rcvList;
    CalendarView calendarView;
    String str;
    TextView tv_date, tv_calorie_remaining_value,tv_present_weight,tv_weight_goal;
    DBHelper dbHelper;
    SQLiteDatabase db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //프래그먼트 메뉴를 인플레이트해주고 컨테이너에 붙여달라는 뜻임
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_calendar, container, false);
        dbHelper = new DBHelper(getContext(), "LifeAndCalorie.db", null, 1);
        db = dbHelper.getWritableDatabase();
        dbHelper.onCreate(db);
        tv_date = (TextView) rootView.findViewById(R.id.tv_date);
        tv_calorie_remaining_value = (TextView) rootView.findViewById(R.id.tv_calorie_remaining_value);
        tv_present_weight = (TextView) rootView.findViewById(R.id.tv_present_weight);
        tv_weight_goal = (TextView) rootView.findViewById(R.id.tv_weight_goal);
        materialCalendarView = rootView.findViewById(R.id.CalendarView);
        materialCalendarView.setSelectedDate(CalendarDay.today());

        rcvList = new ArrayList<>();
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(rcvList, db,materialCalendarView);
        recyclerView.setAdapter(adapter);

        db.execSQL("INSERT INTO USERDATA ('FoodName', 'Calorie', 'DATE', 'Weight') VALUES ('불고기','740','2021-09-05','64.5');");
        db.execSQL("INSERT INTO USERDATA ('FoodName', 'Calorie', 'DATE', 'Weight') VALUES ('햄버거','380','2021-09-05','64.5');");
        db.execSQL("INSERT INTO USERDATA ('FoodName', 'Calorie', 'DATE', 'Weight') VALUES ('치킨','510','2021-09-05','64.5');");
        db.execSQL("INSERT INTO USERDATA ('FoodName', 'Calorie', 'DATE', 'Weight') VALUES ('삼겹살','420','2021-09-07','64.5');");
        db.execSQL("INSERT INTO USERDATA ('FoodName', 'Calorie', 'DATE', 'Weight') VALUES ('피자','660','2021-09-07','64.5');");
        tv_present_weight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //CustomDialog 객체 생성
                CustomDialog dialogCustom = new CustomDialog(getContext());
                //Dialog 밖에 터치 했을 때 Dialog가 꺼짐
                dialogCustom.setCanceledOnTouchOutside(true);
                //Dialog 취소 가능(back버튼)
                dialogCustom.setCancelable(true);
                //CustomDialog의 크기설정
                dialogCustom.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                //DialogCustom show
                dialogCustom.show();
                return false;
            }
        });
        OnDateSelectedListener onDateSelectedListener = new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int calorie_sum = 0;
                rcvList.clear();
                tv_date.setText((date.getMonth()+1) + "월 " + date.getDay() + "일");
                String dateStr = date.getYear() + "-" + String.format("%02d", date.getMonth() + 1) + "-" + String.format("%02d", date.getDay());
                Cursor c = db.rawQuery("SELECT * FROM USERDATA WHERE DATE ='" + dateStr + "';", null);
                while (c.moveToNext()) {
                    Log.i("suyong", "txt: " + c.getString(1) + c.getString(2) + c.getString(3) + c.getString(4));
                    rcvList.add(new UserData(c.getString(1), c.getString(2), c.getString(3), c.getString(4)));
                    calorie_sum += Integer.parseInt(c.getString(2));
                }
                tv_calorie_remaining_value.setText(1500-calorie_sum +"kcal");
                adapter.notifyDataSetChanged();


            }
        };


        materialCalendarView.setOnDateChangedListener(onDateSelectedListener);
        Cursor c = db.rawQuery("SELECT DATE,sum(calorie) FROM USERDATA group by DATE;", null);
        materialCalendarView.removeDecorators();
        materialCalendarView.addDecorators(new SaturdayDecorator(), new SundayDecorator());
        while (c.moveToNext()) {
            materialCalendarView.addDecorator(new SuccessDayDecorator(c.getString(0), c.getString(1)));
        }
        return rootView;

    }
}