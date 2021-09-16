package com.example.team_project.calendar_fragment;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

import androidx.annotation.NonNull;

public class CustomTextSpan implements LineBackgroundSpan {
    Paint paintObj;
    private String text;
    private final String[] color = {"#FF0000", "#00FF00","#0000FF", "#AAAAAA"};
    CustomTextSpan() {
        paintObj = new Paint();
        paintObj.setColor(Color.parseColor(color[3]));
        this.text = "";
    }

    CustomTextSpan(String text) {
        paintObj = new Paint();
        paintObj.setColor(Color.parseColor(color[3]));
        this.text = text;
    }

    @Override
    public void drawBackground(@NonNull Canvas canvas, @NonNull Paint paint, int i, int i1, int i2, int i3, int i4, @NonNull CharSequence charSequence, int i5, int i6, int i7) {
        paintObj.setTextSize(canvas.getHeight()/4);
        paintObj.setTextAlign(Paint.Align.LEFT);
        int x = (int)paintObj.measureText(text);
        int x2 = canvas.getWidth();
        int height = canvas.getHeight();
        canvas.drawText(text, (x2-x)/2,height*4/7,paintObj);



    }

}
