package com.example.life_and_calorie.community.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.life_and_calorie.R;
import com.example.life_and_calorie.community.itemclass.Community_Comment_Item;

import java.util.ArrayList;

public class CommentAdapter extends ArrayAdapter<Community_Comment_Item> {
    LayoutInflater inflater;
    ArrayList<Community_Comment_Item> commentItem;

    public CommentAdapter(@NonNull Context context, int resource, int textViewResourceId, ArrayList<Community_Comment_Item> object) {
        super(context, resource, textViewResourceId, object);
        commentItem = object;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.comment_list_item_layout, null);
        }
        TextView commentItem_text = (TextView) view.findViewById(R.id.commentItem_text);
        TextView commentItem_writer = (TextView) view.findViewById(R.id.commentItem_writer);
        TextView commentItem_date = (TextView) view.findViewById(R.id.commentItem_date);

        commentItem_text.setText(commentItem.get(position).getComment_text());
        commentItem_writer.setText(commentItem.get(position).getComment_writer_ID());
        commentItem_date.setText(commentItem.get(position).getComment_date());

        return view;
    }
}
