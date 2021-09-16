package com.example.team_project.userpage;

import static com.github.mikephil.charting.components.YAxis.AxisDependency.LEFT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.team_project.R;
import com.example.team_project.calendar_fragment.DBHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

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
        View view = inflater.inflate(R.layout.activity_weight_fragment, container, false);
        BarChart chart = view.findViewById(R.id.BarChart);
        ivYearMonthPicker = view.findViewById(R.id.iv_year_month_picker);
        dbHelper = new DBHelper(getContext(), "LifeAndCalorie.db", null, 1);
        TextView tv = view.findViewById(R.id.tv);
        db = dbHelper.getWritableDatabase();
        ArrayList NoOfEmp = new ArrayList();

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String getTime = sdf.format(date);
        //날짜를 선택하지않으면 자동으로 현재 날짜로 설정
        String calendar_date = getTime;
        String present_day[] = calendar_date.split("-");


        DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                String str_month = monthOfYear+"";
                String str_year = year +"";

                NoOfEmp.clear();
                if (monthOfYear < 10) {
                    str_month = "0" + String.valueOf(monthOfYear);
                    str_year = String.valueOf(year);
                }
                else {
                    str_month = String.valueOf(monthOfYear);
                    str_year = String.valueOf(year);
                }
                tv.setText(str_year + "년 " + str_month + "월");
                Cursor c = db.rawQuery("SELECT * FROM WEIGHT;", null);
                while (c.moveToNext()) {
                    String yeardaymonth[] = c.getString(0).split("-");
                    if(str_year.equals(yeardaymonth[0]) == true) {
                        if(str_month.equals(yeardaymonth[1]) == true) {
                            NoOfEmp.add(new BarEntry(Float.parseFloat(c.getString(1)), Integer.parseInt(yeardaymonth[2])-1));
                        }
                    }
                }

                ArrayList Year = new ArrayList();

                for (int i = 0 ; i < arr.length; i++) {
                    Year.add(String.valueOf(arr[i]));
                }
                BarDataSet bardataset = new BarDataSet(NoOfEmp, "몸무게");
                BarData data = new BarData(Year, bardataset);
                bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                data.setValueTextSize(16);
                data.setValueTextColor(Color.BLACK);
                chart.setDrawGridBackground(true);
                // 오른쪽 Y축을 안보이게 함
                YAxis yRAxis = chart.getAxisRight();
                YAxis yLAxis = chart.getAxisLeft();
                XAxis xAxis = chart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                yLAxis.setAxisMinValue(50);
                yLAxis.setAxisMaxValue(150);
                yRAxis.setEnabled(false);
                chart.setEnabled(false);
                chart.setPinchZoom(false);
                chart.animateY(1000);
                chart.setVisibleYRangeMaximum(150, LEFT);
                chart.setData(data);
            }
        };

        Cursor c = db.rawQuery("SELECT * FROM WEIGHT;", null);
        while (c.moveToNext()) {
            String yeardaymonth[] = c.getString(0).split("-");
            if(present_day[0].equals(yeardaymonth[0]) == true) {
                if(present_day[1].equals(yeardaymonth[1]) == true) {
                    NoOfEmp.add(new BarEntry(Float.parseFloat(c.getString(1)), Integer.parseInt(yeardaymonth[2])-1));
                    tv.setText(present_day[0] + "년 " + present_day[1] + "월");
                }
            }
        }

        ArrayList Year = new ArrayList();

        for (int i = 0 ; i < arr.length; i++) {
            Year.add(String.valueOf(arr[i]));
        }
        BarDataSet bardataset = new BarDataSet(NoOfEmp, "몸무게");
        BarData data = new BarData(Year, bardataset);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        data.setValueTextSize(16);
        data.setValueTextColor(Color.BLACK);
        chart.setDrawGridBackground(true);
        // 오른쪽 Y축을 안보이게 함
        YAxis yRAxis = chart.getAxisRight();
        YAxis yLAxis = chart.getAxisLeft();
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        yLAxis.setAxisMinValue(50);
        yLAxis.setAxisMaxValue(150);
        yRAxis.setEnabled(false);
        chart.setEnabled(false);
        chart.setPinchZoom(false);
        chart.animateY(1000);
        chart.setData(data);

        ivYearMonthPicker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                MyYearMonthPickerDialog pd = new MyYearMonthPickerDialog();
                pd.setListener(d);
            }
        });


        ivYearMonthPicker = view.findViewById(R.id.iv_year_month_picker);
        ivYearMonthPicker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                MyYearMonthPickerDialog pd = new MyYearMonthPickerDialog();
                pd.setListener(d);
                pd.show(getActivity().getSupportFragmentManager(), "YearMonthPickerTest");
            }
        });

        return view;
    }
}