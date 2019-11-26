package com.example.comicmap;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.util.ArrayList;

public class CheckListAdapter extends Adapter<CheckListAdapter.ViewHolder> {

    private ArrayList<String> mData;

    public CheckListAdapter(ArrayList<String> list) {
        mData = list;
    }

    public void deleteItem(int position) {
        //mRecentlyDeletedItem = mData.get(position);
        //mRecentlyDeletedItemPosition = position;
        mData.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public CheckListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if(mData.isEmpty()) {
            return 0;
        } else {
            return mData.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
