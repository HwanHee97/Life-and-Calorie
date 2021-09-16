package com.example.team_project.calendar_fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team_project.R;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<UserData> mData = null;
    private MaterialCalendarView materialCalendarView;
    SQLiteDatabase db;
    Context context;
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
                    db.execSQL("DELETE FROM USERDATA WHERE FoodName='"+tv_food.getText()+"';");
                    Log.i("suyong","Removed!");
                    Cursor c = db.rawQuery("SELECT DATE,sum(calorie) FROM USERDATA group by DATE;", null);
                    materialCalendarView.removeDecorators();
                    materialCalendarView.addDecorators(new SaturdayDecorator(), new SundayDecorator());
                    while (c.moveToNext()) {
                        materialCalendarView.addDecorator(new SuccessDayDecorator(c.getString(0), c.getString(1)));
                    }
                    materialCalendarView.invalidateDecorators();

                }
            });

        }
    }
    RecyclerViewAdapter(ArrayList<UserData> list, SQLiteDatabase db, MaterialCalendarView materialCalendarView){
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
