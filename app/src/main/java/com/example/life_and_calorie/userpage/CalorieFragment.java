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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalorieFragment extends Fragment {
    private static final int MAX_YEAR = 2099;
    private static final int MIN_YEAR = 1980;
    int[] arr = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
    DBHelper dbHelper;
    SQLiteDatabase db;
    ImageView ivYearMonthPicker;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(getContext(), "LifeAndCalorie.db", null, 1);
        db = dbHelper.getWritableDatabase();
        View create_view = inflater.inflate(R.layout.activity_calorie_fragment, container, false);


        ImageButton btn_before = create_view.findViewById(R.id.btn_before);
        ImageButton btn_after = create_view.findViewById(R.id.btn_after);
        TextView tv_year = create_view.findViewById(R.id.tv_year);
        TextView tv_month = create_view.findViewById(R.id.tv_month);

        ArrayList<Entry> entries = new ArrayList<>();

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
                String str_month;
                String str_year;
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
        Cursor c = db.rawQuery("SELECT * FROM USERDATA group by DATE;", null);
        ArrayList<Entry> NoOfEmp = new ArrayList<>();
        while (c.moveToNext()) {
            String yeardaymonth[] = c.getString(3).split("-");
            if(year.equals(yeardaymonth[0]) == true) {
                if(month.equals(yeardaymonth[1]) == true) {
                    NoOfEmp.add(new Entry(Float.parseFloat(c.getString(2)), Integer.parseInt(yeardaymonth[2])));
                }
            }
        }
        return NoOfEmp;
    }

    public void Draw_Chart(View view, ArrayList entries) {
        List<Entry> entries2 = new ArrayList<>();
        SharedPreferences pref = getActivity().getSharedPreferences("sharedpreferences", getActivity().MODE_PRIVATE);
        int calorie = pref.getInt("calorie",0);

        for(int i = 0 ; i < arr.length ; i++) {
            entries2.add(new Entry(calorie,i));
        }
        LineDataSet set2 = new LineDataSet(entries2, "설정한 칼로리 (kcal)");

        LineChart lineChart = (LineChart) view.findViewById(R.id.LineChart);
        LineDataSet lineDataSet = new LineDataSet(entries, "칼로리 (kcal)");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add((ILineDataSet) lineDataSet);
        lineDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        lineDataSet.setValueTextSize(12);
        lineDataSet.setLineWidth(3);
        lineDataSet.setCircleRadius(3);

        ArrayList<String> Year = new ArrayList<String>();
        for (int i = 0 ; i < arr.length; i++) {
            Year.add(String.valueOf(arr[i]));
        }
        LineData lineData = new LineData(Year, dataSets);

        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);

        // 최대 최소값
        yLAxis.setAxisMaxValue(calorie+2000);
        yLAxis.setAxisMinValue(0);

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(true);
        yRAxis.setDrawGridLines(false);
        yRAxis.setDrawAxisLine(false);
        lineChart.setDoubleTapToZoomEnabled(true);
        lineChart.setDrawGridBackground(true);
        lineChart.setTouchEnabled(true);
        lineChart.setPinchZoom(false);
        lineChart.setGridBackgroundColor(Color.parseColor("#F8FAE4"));
        xAxis.setDrawGridLines(false);

        lineDataSet.setDrawValues(false);
        MyMarkerView marker = new MyMarkerView(getContext(), R.layout.activity_my_marker_view);
        lineChart.setMarkerView(marker);

        // x축, y축 간격
        yLAxis.setGranularity(1.0f);
        lineChart.animateXY(500,500);
        set2.setDrawValues(false);
        set2.setDrawCircles(false);
        set2.setColor(Color.parseColor("#658361"));
        set2.setLineWidth(1);

        lineData.addDataSet(set2);
        lineChart.invalidate();
    }

}