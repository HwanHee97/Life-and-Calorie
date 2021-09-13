package com.example.team_project.userpage;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.team_project.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class CalorieFragment extends Fragment {
    private static final int MAX_YEAR = 2099;
    private static final int MIN_YEAR = 1980;
    int[] arr = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};

    Button btnYearMonthPicker;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_calorie_fragment, container, false);
        LineChart lineChart = (LineChart) view.findViewById(R.id.LineChart);
        DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                Log.i("sjh", "year = " + year + ", month = " + monthOfYear + ", day = " + dayOfMonth);
            }
        };

        // 칼로리 기준 선
        List<Entry> entries2 = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i++) {
            entries2.add(new Entry(1500, i));
        }
        LineDataSet set2 = new LineDataSet(entries2, "설정한 칼로리");


        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(765, 0));
        entries.add(new Entry(1050, 1));
        entries.add(new Entry(2020,2));
        entries.add(new Entry(980, 3));
        entries.add(new Entry(600, 4));
        entries.add(new Entry(600, 5));
        entries.add(new Entry(600, 6));


        LineDataSet lineDataSet = new LineDataSet(entries, "칼로리");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add((ILineDataSet) lineDataSet);
        lineDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        lineDataSet.setValueTextSize(0);
        lineDataSet.setLineWidth(5);
        lineDataSet.setCircleRadius(3);

        ArrayList<String> year = new ArrayList<String>();
        for (int i = 0 ; i < arr.length; i++) {
            year.add(String.valueOf(arr[i]));
        }
        LineData lineData = new LineData(year, dataSets);

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
        lineChart.setDrawGridBackground(false);
        lineChart.setTouchEnabled(true);
        lineChart.setPinchZoom(false);
        xAxis.setDrawGridLines(false);

        // x축, y축 간격
        yLAxis.setGranularity(1.0f);

        lineChart.animateXY(1000,1000);

        set2.setDrawValues(false);
        set2.setDrawCircles(false);
        lineData.addDataSet(set2);
        lineChart.invalidate();



        btnYearMonthPicker = view.findViewById(R.id.btn_year_month_picker);
        btnYearMonthPicker.setOnClickListener(new View.OnClickListener(){
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