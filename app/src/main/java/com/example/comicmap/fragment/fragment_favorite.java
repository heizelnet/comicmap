package com.example.comicmap.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicmap.DataBaseHelper;
import com.example.comicmap.MyApplication;
import com.example.comicmap.OAuth.APIClient;
import com.example.comicmap.OAuth.TokenProcess;
import com.example.comicmap.R;
import com.example.comicmap.SwipeDeleter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class fragment_favorite extends Fragment {

    private ViewGroup viewGroup;
    private RecyclerView recyclerView;
    private SQLiteDatabase mDataBase;
    private TextView textView;
    private TokenProcess apiInterface;
    private retrofit2.Call<ResponseBody> responseBodyCall;
    private circle_info_adapter adapter;

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
        textView = view.findViewById(R.id.textView_favorite_state);

        //Test Code for update
        /*
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
         */
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

        ArrayList<circle_instance> items = new ArrayList<>();
        String query = "select * from circle_info where favorite > 0";

        Cursor cur = mDataBase.rawQuery(query, null);
        cur.moveToFirst();

        //iterate query add items to dialog
        if (cur.getCount() != 0) {
            while (true) {
                try {
                    items.add(new circle_instance(cur.getInt(cur.getColumnIndex("wid")), cur.getString(cur.getColumnIndex("Name")),
                            cur.getString(cur.getColumnIndex("Author")),
                            cur.getString(cur.getColumnIndex("Genre")), cur.getString(cur.getColumnIndex("Day")),
                            cur.getString(cur.getColumnIndex("circle")), cur.getInt(cur.getColumnIndex("IsPixivRegistered")),
                            cur.getInt(cur.getColumnIndex("IsTwitterRegistered")), cur.getInt(cur.getColumnIndex("IsNiconicoRegistered")),
                            cur.getString(cur.getColumnIndex("PixivUrl")), cur.getString(cur.getColumnIndex("TwitterUrl")),
                            cur.getString(cur.getColumnIndex("NiconicoUrl"))));
                } catch (Exception e) {
                        break;
                }
                cur.moveToNext();
            }
            adapter = new circle_info_adapter(items);
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

}
