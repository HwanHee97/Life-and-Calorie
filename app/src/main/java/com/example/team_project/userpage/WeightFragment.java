package com.example.team_project.userpage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.team_project.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class WeightFragment extends Fragment {
    int[] arr = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
    Button btnYearMonthPicker;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_weight_fragment, container, false);
        BarChart chart = view.findViewById(R.id.BarChart);

        DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                Log.i("sjh", "year = " + year + ", month = " + monthOfYear + ", day = " + dayOfMonth);
            }
        };
        btnYearMonthPicker = view.findViewById(R.id.btn_year_month_picker);
        btnYearMonthPicker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                MyYearMonthPickerDialog pd = new MyYearMonthPickerDialog();
                pd.setListener(d);
            }
        });

        ArrayList NoOfEmp = new ArrayList();
        NoOfEmp.add(new BarEntry(954f,0));
        NoOfEmp.add(new BarEntry(1000f,1));
        NoOfEmp.add(new BarEntry(1072f,2));
        NoOfEmp.add(new BarEntry(970f,3));
        NoOfEmp.add(new BarEntry(1230f,4));
        NoOfEmp.add(new BarEntry(1800f,5));
        NoOfEmp.add(new BarEntry(1000f,6));
        NoOfEmp.add(new BarEntry(950f,7));
        NoOfEmp.add(new BarEntry(1700f,8));

        ArrayList year = new ArrayList();

        for (int i = 0 ; i < arr.length; i++) {
            year.add(String.valueOf(arr[i]));
        }
        BarDataSet bardataset = new BarDataSet(NoOfEmp, "몸무게");
        BarData data = new BarData(year, bardataset);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        data.setValueTextSize(16);
        data.setValueTextColor(Color.BLACK);
        chart.setDrawGridBackground(true);
        // 오른쪽 Y축을 안보이게 함
        YAxis yRAxis = chart.getAxisRight();
        yRAxis.setEnabled(false);
        chart.setEnabled(false);
        chart.setPinchZoom(false);
        chart.animateY(1000);
        chart.setData(data);

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