package com.hazelnut.comicmap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ItemSpinnerAdapter extends ArrayAdapter<String> {

    private String[] Names;
    private Context context;

    public ItemSpinnerAdapter(@NonNull Context context, String[] names) {
        super(context, R.layout.spinner_row);
        this.context= context;
        this.Names = names;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return Names.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();

        if (convertView == null) {

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.spinner_row, parent, false);

            mViewHolder.mName = (TextView) convertView.findViewById(R.id.textView_spinner_name);
            convertView.setTag(mViewHolder);

        } else {

            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.mName.setText(Names[position]);

        return convertView;
    }

    private static class ViewHolder {
        TextView mName;
    }
}
