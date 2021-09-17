package com.example.life_and_calorie.userpage;

import android.content.Context;
import android.widget.TextView;

import com.example.life_and_calorie.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

public class MyMarkerView extends MarkerView {
    private TextView tvContent;

//        private void initAdapter() {
//            mAdapter = new StackA
//        }

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        tvContent = (TextView)findViewById(R.id.tvContent);
        System.out.println(layoutResource);
        System.out.println(context);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setText("" + e.getVal());
    }

    @Override
    public int getXOffset(float xpos) {
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset(float ypos) {
        return -getHeight();
    }
}
