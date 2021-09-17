package com.example.life_and_calorie.userpage;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.life_and_calorie.R;
import com.example.life_and_calorie.calendar_fragment.DBHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeightFragment extends Fragment {
    int[] arr = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
    ImageView ivYearMonthPicker;
    DBHelper dbHelper;
    SQLiteDatabase db;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View create_view = inflater.inflate(R.layout.activity_weight_fragment, container, false);
        ivYearMonthPicker = create_view.findViewById(R.id.iv_year_month_picker);
        dbHelper = new DBHelper(getContext(), "LifeAndCalorie.db", null, 1);
        ImageButton btn_before = create_view.findViewById(R.id.btn_before);
        ImageButton btn_after = create_view.findViewById(R.id.btn_after);
        TextView tv_year = create_view.findViewById(R.id.tv_year);
        TextView tv_month = create_view.findViewById(R.id.tv_month);
        db = dbHelper.getWritableDatabase();
        ArrayList entries = new ArrayList();

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String getTime = sdf.format(date);
        //날짜를 선택하지않으면 자동으로 현재 날짜로 설정
        String calendar_date = getTime;
        String present_day[] = calendar_date.split("-");

        tv_year.setText(present_day[0]);
        tv_month.setText(present_day[1]);
        Draw_Chart(create_view,Insert_Data(String.valueOf(present_day[0]), String.valueOf(present_day[1])));

        // 이전달 버튼
        btn_before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = Integer.parseInt(tv_year.getText().toString());
                int month = Integer.parseInt(tv_month.getText().toString());
                entries.clear();

                month--;
                if(month==0){
                    month = 12;
                    year--;
                }
                tv_year.setText(year+"");
                tv_month.setText(String.format("%02d",month));
                Draw_Chart(create_view,Insert_Data(String.valueOf(year), String.format("%02d",month)));
            }
        });

        // 이후달 버튼
        btn_after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = Integer.parseInt(tv_year.getText().toString());
                int month = Integer.parseInt(tv_month.getText().toString());
                entries.clear();

                month++;
                if(month==13){
                    month = 1;
                    year++;
                }
                tv_year.setText(year+"");
                tv_month.setText(String.format("%02d",month));
                Draw_Chart(create_view,Insert_Data(String.valueOf(year), String.format("%02d",month)));
            }
        });

        DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker View, int year, int monthOfYear, int dayOfMonth){
                String str_month = monthOfYear+"";
                String str_year = year +"";
                entries.clear();
                if (monthOfYear < 10) {
                    str_month = "0" + String.valueOf(monthOfYear);
                    str_year = String.valueOf(year);
                }
                else {
                    str_month = String.valueOf(monthOfYear);
                    str_year = String.valueOf(year);
                }
                tv_year.setText(str_year);
                tv_month.setText(str_month);
                Draw_Chart(create_view,Insert_Data(str_year, str_month));
            }
        };


        ivYearMonthPicker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                MyYearMonthPickerDialog pd = new MyYearMonthPickerDialog();
                pd.setListener(d);
            }
        });

        ivYearMonthPicker = create_view.findViewById(R.id.iv_year_month_picker);
        ivYearMonthPicker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                MyYearMonthPickerDialog pd = new MyYearMonthPickerDialog();
                pd.setListener(d);
                pd.show(getActivity().getSupportFragmentManager(), "YearMonthPickerTest");
            }
        });
        return create_view;
    }

    // 데이터 삽입 함수
    public ArrayList<Entry> Insert_Data(String year, String month) {
        ArrayList NoOfEmp = new ArrayList();
        Cursor c = db.rawQuery("SELECT * FROM WEIGHT;", null);
        while (c.moveToNext()) {
            String yeardaymonth[] = c.getString(0).split("-");
            if(year.equals(yeardaymonth[0])) {
                if(month.equals(yeardaymonth[1])) {
                    NoOfEmp.add(new BarEntry(Float.parseFloat(c.getString(1)), Integer.parseInt(yeardaymonth[2])-1));
                }
            }
        }
        return NoOfEmp;
    }

    public void Draw_Chart(View view, ArrayList NoOfEmp) {
        ArrayList Year = new ArrayList();

        for (int i = 0 ; i < arr.length; i++) {
            Year.add(String.valueOf(arr[i]));
        }

        BarChart chart = view.findViewById(R.id.BarChart);
        SharedPreferences pref = getActivity().getSharedPreferences("sharedpreferences", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        float weight = pref.getFloat("weight",0);

        BarDataSet bardataset = new BarDataSet(NoOfEmp, "몸무게 (kg)");
        BarData data = new BarData(Year, bardataset);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

        chart.setGridBackgroundColor(Color.parseColor("#F8FAE4"));
        data.setValueTextSize(16);
        data.setValueTextColor(Color.BLACK);

        chart.setDrawGridBackground(true);
        // 오른쪽 Y축을 안보이게 함
        YAxis yRAxis = chart.getAxisRight();
        YAxis yLAxis = chart.getAxisLeft();
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        yLAxis.setAxisMinValue(0);
        yLAxis.setAxisMaxValue(weight+30);
        yRAxis.setDrawLabels(true);
        yRAxis.setDrawGridLines(false);
        yRAxis.setDrawAxisLine(false);
        chart.setEnabled(true);
        chart.setPinchZoom(false);
        chart.animateY(500);

        bardataset.setDrawValues(false);
        MyMarkerView marker = new MyMarkerView(getContext(), R.layout.activity_my_marker_view);
        chart.setMarkerView(marker);

        chart.setData(data);
    }
}