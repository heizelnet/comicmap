package com.heizelnet.comicmap.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.heizelnet.comicmap.DataBaseHelper;
import com.heizelnet.comicmap.MyApplication;
import com.heizelnet.comicmap.R;

import java.util.ArrayList;

public class fragment_favorite extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ViewGroup viewGroup;
    private RecyclerView recyclerView;
    private SQLiteDatabase mDataBase;
    private TextView textView;
    private favorite_info_adapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

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

        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        viewGroup = view.findViewById(R.id.empty_group);
        recyclerView = view.findViewById(R.id.recview);
        textView = view.findViewById(R.id.textView_favorite_state);
        show_favorite();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void show_favorite() {
        textView.setText("Loading..");

        ArrayList<favorite_instance> items = new ArrayList<>();
        String query = "select * from circle_info where favorite > 0 order by favorite asc";

        Cursor cur = mDataBase.rawQuery(query, null);
        cur.moveToFirst();

        //iterate query add items to dialog
        if (cur.getCount() != 0) {
            while (true) {
                try {
                    items.add(new favorite_instance(cur.getInt(cur.getColumnIndex("wid")), cur.getString(cur.getColumnIndex("Name")),
                            cur.getString(cur.getColumnIndex("Author")),
                            cur.getString(cur.getColumnIndex("Genre")), cur.getString(cur.getColumnIndex("Day")),
                            cur.getString(cur.getColumnIndex("circle")), cur.getInt(cur.getColumnIndex("IsPixivRegistered")),
                            cur.getInt(cur.getColumnIndex("IsTwitterRegistered")), cur.getInt(cur.getColumnIndex("IsNiconicoRegistered")),
                            cur.getString(cur.getColumnIndex("PixivUrl")), cur.getString(cur.getColumnIndex("TwitterUrl")),
                            cur.getString(cur.getColumnIndex("NiconicoUrl")), cur.getString(cur.getColumnIndex("memo"))));
                } catch (Exception e) {
                        break;
                }
                cur.moveToNext();
            }
            adapter = new favorite_info_adapter(items);
            setUpRecyclerView();

            viewGroup.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            textView.setText("Empty!");
        }

    }

    private void setUpRecyclerView() {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyApplication.getAppContext()));
        /*
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeDeleter(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
         */
    }

    @Override
    public void onRefresh() {
        show_favorite();
        swipeRefreshLayout.setRefreshing(false);
    }
}
