package com.example.comicmap;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.util.ArrayList;

public class CheckListAdapter extends Adapter<CheckListAdapter.ViewHolder> {

    private ArrayList<String> mData;
    private DataSharedPreference pref = new DataSharedPreference();

    public CheckListAdapter() {
        mData = pref.getStringArrayList(pref.CHECKLIST_KEY);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1 ;
        CheckBox checkBox;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            textView1 = itemView.findViewById(R.id.textView_Checklist) ;
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

    public void deleteItem(int position) {
        //mRecentlyDeletedItem = mData.get(position);
        //mRecentlyDeletedItemPosition = position;
        mData.remove(position);
        notifyItemRemoved(position);
        pref.setStringArrayList(pref.CHECKLIST_KEY, mData);
    }

    public void addItem(int position, String item) {
        mData.add(item);
        notifyItemInserted(position);
        pref.setStringArrayList(pref.CHECKLIST_KEY, mData);
    }

    @NonNull
    @Override
    public CheckListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.checklist_item, parent, false) ;
        CheckListAdapter.ViewHolder vh = new CheckListAdapter.ViewHolder(view);

        vh.checkBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if(isChecked)
                vh.textView1.setPaintFlags(vh.textView1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            else
                vh.textView1.setPaintFlags(0);
        });

        return vh ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = mData.get(position) ;
        holder.textView1.setText(text) ;
    }

    @Override
    public int getItemCount() {
        if(mData.isEmpty()) {
            return 0;
        } else {
            return mData.size();
        }
    }

}
