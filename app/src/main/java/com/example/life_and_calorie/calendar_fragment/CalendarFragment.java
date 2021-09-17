package com.example.life_and_calorie.calendar_fragment;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.life_and_calorie.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;

public class CalendarFragment extends Fragment {
    MaterialCalendarView materialCalendarView;
    ArrayList<UserData> rcvList;
    private String dateStr;
    TextView tv_date, tv_calorie_remaining_value,tv_present_weight,tv_weight_goal,tv_calorie_goal_value;
    DBHelper dbHelper;
    SQLiteDatabase db;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //프래그먼트 메뉴를 인플레이트해주고 컨테이너에 붙여달라는 뜻임
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_calendar, container, false);
        pref = getActivity().getSharedPreferences("sharedpreferences", getActivity().MODE_PRIVATE);
        editor = pref.edit();
        dbHelper = new DBHelper(getContext(), "LifeAndCalorie.db", null, 1);
        db = dbHelper.getWritableDatabase();

        tv_date = (TextView) rootView.findViewById(R.id.tv_date);
        tv_calorie_goal_value = (TextView)rootView.findViewById(R.id.tv_calorie_goal_value);
        tv_calorie_remaining_value = (TextView) rootView.findViewById(R.id.tv_calorie_remaining_value);
        tv_present_weight = (TextView) rootView.findViewById(R.id.tv_present_weight);
        tv_weight_goal = (TextView) rootView.findViewById(R.id.tv_weight_goal);

        materialCalendarView = rootView.findViewById(R.id.CalendarView);
        materialCalendarView.setSelectedDate(CalendarDay.today());
        tv_calorie_goal_value.setText(pref.getInt("calorie",0)+"");
        tv_weight_goal.setText(pref.getFloat("weight",0.0F)+"");
        rcvList = new ArrayList<>();
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(rcvList, db,materialCalendarView,getActivity());
        recyclerView.setAdapter(adapter);

        tv_present_weight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //CustomDialog 객체 생성
                CustomDialog dialogCustom = new CustomDialog(getContext(),dateStr, tv_present_weight);
                //Dialog 밖에 터치 했을 때 Dialog가 꺼짐
                dialogCustom.setCanceledOnTouchOutside(true);
                //Dialog 취소 가능(back버튼)
                dialogCustom.setCancelable(true);
                //CustomDialog의 크기설정
                dialogCustom.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogCustom.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                //DialogCustom show
                dialogCustom.show();
                CalendarDay calday = materialCalendarView.getSelectedDate();
                return false;
            }
        });

        OnDateSelectedListener onDateSelectedListener = new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int calorie_sum = 0;
                rcvList.clear();
                tv_date.setText((date.getMonth()+1) + "월 " + date.getDay() + "일");
                dateStr = date.getYear() + "-" + String.format("%02d", date.getMonth() + 1) + "-" + String.format("%02d", date.getDay());
                Cursor c = db.rawQuery("SELECT * FROM USERDATA WHERE DATE ='" + dateStr + "';", null);
                while (c.moveToNext()) {
                    rcvList.add(new UserData(c.getString(1), c.getString(2), c.getString(3)));
                    calorie_sum += Integer.parseInt(c.getString(2));
                }
                tv_calorie_remaining_value.setText(pref.getInt("calorie",0)-calorie_sum +"kcal");

                c = db.rawQuery("SELECT * FROM WEIGHT WHERE DATE ='" + dateStr + "';",null);
                tv_present_weight.setText("Click here");
                while(c.moveToNext()){
                    tv_present_weight.setText(c.getString(1));
                }
                adapter.notifyDataSetChanged();
            }
        };
        materialCalendarView.setOnDateChangedListener(onDateSelectedListener);
        onDateSelectedListener.onDateSelected(materialCalendarView,CalendarDay.today(),true);
        materialCalendarView.removeDecorators();
        materialCalendarView.addDecorators(new SaturdayDecorator(), new SundayDecorator(), new SelectDecorator(getActivity()));
        Cursor c = db.rawQuery("SELECT DATE,sum(calorie) FROM USERDATA group by DATE;", null);
        while (c.moveToNext()) {
            materialCalendarView.addDecorator(new UserDataDecorator(c.getString(0), c.getString(1)));
        }
        return rootView;

    }
}