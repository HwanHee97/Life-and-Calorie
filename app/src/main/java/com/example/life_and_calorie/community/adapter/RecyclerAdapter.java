package com.example.life_and_calorie.community.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.life_and_calorie.R;
import com.example.life_and_calorie.community.ShowWriting;
import com.example.life_and_calorie.community.itemclass.RecyclerItem;

import java.util.ArrayList;
//import java.util.Date;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private ArrayList<RecyclerItem> mData;
    Context context;

    public RecyclerAdapter(ArrayList<RecyclerItem> data) {//List 객체받는다.
        mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//2.뷰홀더 생성
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recycler_community_item_layout, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {//3.생성한뷰홀더에 바인딩
        RecyclerItem tmp_item = mData.get(position);
        holder.title.setText(tmp_item.getTitle());
        holder.writer_ID.setText(tmp_item.getWriter_ID());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {//1.뷰홀더 정의
        TextView title;
        TextView writer_ID;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.RecyclerItem_title);
            writer_ID = itemView.findViewById(R.id.RecyclerItem_writer);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(view.getContext(), ShowWriting.class);
                        intent.putExtra("community_item", mData.get(pos));
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
