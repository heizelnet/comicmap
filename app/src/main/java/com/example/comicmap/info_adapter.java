package com.example.comicmap;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class info_adapter extends RecyclerView.Adapter<info_adapter.ItemViewHolder> {

    private ArrayList<circle> mData = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.circle_info_item, parent, false);
        info_adapter.ItemViewHolder vh = new info_adapter.ItemViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        circle data = mData.get(position);

        Typeface face = ResourcesCompat.getFont(holder.itemView.getContext(), R.font.hannari);
        try {
            Glide.with(holder.itemView.getContext()).load(data.getUrl()).into(holder.imageView);
        } catch (Exception e) {
            Glide.with(holder.itemView.getContext()).load(R.drawable.map_icon).into(holder.imageView);
        }
        Log.e("exploit", "image loading");
        holder.textName2.setTypeface(face);
        holder.textName2.setText("Name : " + data.getName());
        holder.textAuthor2.setTypeface(face);
        holder.textAuthor2.setText("Author : " + data.getAuthor());
        holder.textHall2.setTypeface(face);
        holder.textHall2.setText("Hall : " + data.getHall());
        holder.textDay2.setTypeface(face);
        holder.textDay2.setText("Day : " + data.getDay());
        holder.textCircle.setTypeface(face);
        holder.textCircle.setText("Circle : " + data.getCircle());


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setItems(ArrayList<circle> items) {
        this.mData = items;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textName2, textAuthor2, textHall2, textDay2, textCircle;
        ImageView imageView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageThumb);
            textName2 = itemView.findViewById(R.id.text_Name2);
            textAuthor2 = itemView.findViewById(R.id.text_Author2);
            textHall2 = itemView.findViewById(R.id.text_Hall2);
            textDay2 = itemView.findViewById(R.id.text_Day2);
            textCircle = itemView.findViewById(R.id.text_Circle);

        }
    }
}
