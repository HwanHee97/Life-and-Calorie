package com.example.life_and_calorie.calendar_fragment;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.life_and_calorie.R;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<UserData> mData = null;
    private MaterialCalendarView materialCalendarView;

    SQLiteDatabase db;
    Activity context;
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_food, tv_calorie;
        ImageView iv_remove;
        ViewHolder(View itemView){
            super(itemView);
            tv_food = itemView.findViewById(R.id.tv_food);
            tv_calorie = itemView.findViewById(R.id.tv_calorie);
            iv_remove = itemView.findViewById(R.id.iv_remove);

            iv_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    mData.remove(pos);
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos,mData.size());
                    int day = materialCalendarView.getSelectedDate().getDay();
                    int month = materialCalendarView.getSelectedDate().getMonth()+1;
                    int year = materialCalendarView.getSelectedDate().getYear();
                    String str_date = year + "-" + String.format("%02d",month)+ "-" + String.format("%02d",day);
                    db.execSQL("DELETE FROM USERDATA WHERE FoodName='"+tv_food.getText()+"' AND Date = '"+str_date+"';");
                    Cursor c = db.rawQuery("SELECT DATE,sum(calorie) FROM USERDATA group by DATE;", null);
                    materialCalendarView.removeDecorators();
                    materialCalendarView.addDecorators(new SaturdayDecorator(), new SundayDecorator(), new SelectDecorator(context));
                    while (c.moveToNext()) {
                        materialCalendarView.addDecorator(new UserDataDecorator(c.getString(0), c.getString(1)));
                    }
                    materialCalendarView.invalidateDecorators();

                }
            });

        }
    }
    RecyclerViewAdapter(ArrayList<UserData> list, SQLiteDatabase db, MaterialCalendarView materialCalendarView, Activity context){
        this.context = context;
        mData = list;
        this.db = db;
        this.materialCalendarView = materialCalendarView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recyclerview_row,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String foodName = mData.get(position).getFoodName();
        holder.tv_food.setText(foodName);
        String calorie = mData.get(position).getCalorie();
        holder.tv_calorie.setText(calorie);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


}
