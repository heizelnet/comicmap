package com.example.comicmap.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicmap.DataBaseHelper;
import com.example.comicmap.R;

public class fragment_favorite extends Fragment {

    private ViewGroup viewGroup;
    private RecyclerView recyclerView;
    private SQLiteDatabase mDataBase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        mDataBase = new DataBaseHelper(getContext()).openDataBase();

        viewGroup = view.findViewById(R.id.empty_group);
        recyclerView = view.findViewById(R.id.recview);

        //Test Code for update

        String query = "select * from circle_info where favorite != 0";
        Cursor cur = mDataBase.rawQuery(query, null);
        if(cur.getCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            viewGroup.setVisibility(View.VISIBLE);
        } else {
            viewGroup.setVisibility(View.GONE);
        }
        cur.close();
        mDataBase.close();

        recyclerView.setVisibility(View.GONE);
        viewGroup.setVisibility(View.VISIBLE);

        //Json to DataArray -> show RecyclerView
        return view;
    }
}
