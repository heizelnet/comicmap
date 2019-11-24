package com.example.comicmap;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class circle_info_adapter extends RecyclerView.Adapter<circle_info_adapter.ItemViewHolder> {

    private ArrayList<circle_instance> mData = new ArrayList<>();
    private Context context;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.circle_info_item, parent, false);
        circle_info_adapter.ItemViewHolder vh = new circle_info_adapter.ItemViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        circle_instance data = mData.get(position);
        Glide.with(holder.itemView.getContext()).load(R.drawable.map_icon).into(holder.imageView);
        Log.e("exploit", "image loading");

        Resources res = context.getResources();
        holder.textName2.setText(res.getString(R.string.row_Name, data.getName()));
        holder.textAuthor2.setText(res.getString(R.string.row_Author, data.getAuthor()));
        holder.textHall2.setText(res.getString(R.string.row_Hall, data.getHall()));
        holder.textDay2.setText(res.getString(R.string.row_Day, data.getDay()));
        holder.textCircle.setText(res.getString(R.string.row_Circle, data.getCircle()));


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setItems(ArrayList<circle_instance> items) {
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
