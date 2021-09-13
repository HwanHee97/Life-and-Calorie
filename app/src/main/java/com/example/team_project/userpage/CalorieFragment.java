package com.example.team_project.userpage;

import static com.github.mikephil.charting.components.YAxis.AxisDependency.LEFT;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.team_project.R;
import com.example.team_project.calendar_fragment.DBHelper;
import com.example.team_project.calendar_fragment.SuccessDayDecorator;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Text;

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
        View view = inflater.inflate(R.layout.activity_calorie_fragment, container, false);
        LineChart lineChart = (LineChart) view.findViewById(R.id.LineChart);
        TextView tv = (TextView) view.findViewById(R.id.tv);
        ArrayList<Entry> entries = new ArrayList<>();
        List<Entry> entries2 = new ArrayList<>();

        SharedPreferences pref = getActivity().getSharedPreferences("sharedpreferences", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String getTime = sdf.format(date);
        //날짜를 선택하지않으면 자동으로 현재 날짜로 설정
        String calendar_date = getTime;
        String present_day[] = calendar_date.split("-");
        // 칼로리 기준 선

        for(int i = 0 ; i < arr.length ; i++) {
            entries2.add(new Entry(pref.getInt("calorie",0),i));
        }
        LineDataSet set2 = new LineDataSet(entries2, "설정한 칼로리");

        DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
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
                tv.setText(str_year + "년 " + str_month + "월");
                Cursor c = db.rawQuery("SELECT * FROM USERDATA group by DATE;", null);
                while (c.moveToNext()) {
                    String yeardaymonth[] = c.getString(3).split("-");
                    if(str_year.equals(yeardaymonth[0]) == true) {
                        if(str_month.equals(yeardaymonth[1]) == true) {
                            entries.add(new Entry(Float.parseFloat(c.getString(2)), Integer.parseInt(yeardaymonth[2])));
                        }
                    }
                }
                LineDataSet lineDataSet = new LineDataSet(entries, "칼로리");
                lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add((ILineDataSet) lineDataSet);
                lineDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                lineDataSet.setValueTextSize(12);
                lineDataSet.setLineWidth(5);
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
                YAxis yRAxis = lineChart.getAxisRight();
                yRAxis.setDrawLabels(false);
                yRAxis.setDrawAxisLine(false);
                yRAxis.setDrawGridLines(false);
                lineChart.setDoubleTapToZoomEnabled(false);
                lineChart.setDrawGridBackground(true);
                lineChart.setTouchEnabled(false);
                lineChart.setPinchZoom(false);
                xAxis.setDrawGridLines(false);

                // x축, y축 간격
                yLAxis.setGranularity(1.0f);
                lineChart.animateXY(1000,1000);
                set2.setDrawValues(false);
                set2.setDrawCircles(false);
                set2.setColor(Color.RED);
                set2.setLineWidth(10);
                lineData.addDataSet(set2);
                lineChart.invalidate();
            }
        };

        Cursor c = db.rawQuery("SELECT * FROM USERDATA group by DATE;", null);
        while (c.moveToNext()) {
            String yeardaymonth[] = c.getString(3).split("-");
            if(present_day[0].equals(yeardaymonth[0]) == true) {
                if(present_day[1].equals(yeardaymonth[1]) == true) {
                    entries.add(new Entry(Float.parseFloat(c.getString(2)), Integer.parseInt(yeardaymonth[2])));
                    tv.setText(present_day[0] + "년 " + present_day[1] + "월");
                }
            }
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "칼로리");
        lineDataSet.setLineWidth(7);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add((ILineDataSet) lineDataSet);
        lineDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        lineDataSet.setValueTextSize(12);
        lineDataSet.setLineWidth(5);
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
        yLAxis.setAxisMaxValue(4000);
        yLAxis.setAxisMinValue(0);

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(true);
        lineChart.setTouchEnabled(false);
        lineChart.setPinchZoom(false);
        xAxis.setDrawGridLines(false);

        // x축, y축 간격
        yLAxis.setGranularity(1.0f);
        lineChart.animateXY(1000,1000);
        set2.setDrawValues(false);
        set2.setDrawCircles(false);
        set2.setColor(Color.DKGRAY);
        set2.setLineWidth(6);
        lineData.addDataSet(set2);
        lineChart.invalidate();

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