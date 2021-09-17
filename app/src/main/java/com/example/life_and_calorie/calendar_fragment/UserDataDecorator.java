package com.example.life_and_calorie.calendar_fragment;

import android.graphics.Color;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

public class UserDataDecorator implements DayViewDecorator {
    private final String date;
    private final String calorie;

    UserDataDecorator(String date, String calorie) {
        this.date = date;
        this.calorie = calorie;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        String str = day.getYear() + "-" + String.format("%02d", day.getMonth() + 1) + "-" + String.format("%02d", day.getDay());
        return date.equals(str);
    }

    @Override
    public void decorate(DayViewFacade view) {
        if (Integer.parseInt(calorie) > 1500) {
            view.addSpan(new DotSpan(5, Color.RED));
        } else {
            view.addSpan(new DotSpan(5, Color.GREEN));
        }

        view.addSpan(new CustomTextSpan(calorie));
    }


}
