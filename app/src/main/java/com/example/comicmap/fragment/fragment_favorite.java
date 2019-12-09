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

        recyclerView.setVisibility(View.GONE);
        viewGroup.setVisibility(View.VISIBLE);

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
        apiInterface = APIClient.getClient(TokenProcess.API_URL).create(TokenProcess.class);
        responseBodyCall = apiInterface.getFavoriteList(getResources().getString(R.string.event_id), getResources().getInteger(R.integer.event_no), 0);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                try {
                    String jString = response.body().string();
                    JSONObject res1 = new JSONObject(jString).getJSONObject("response");
                    if(res1.getInt("maxcount") == 0) {
                        textView.setText("Empty!");
                    } else {
                        //Add Items
                        JSONArray list = res1.getJSONArray("list");
                        ArrayList<circle_instance> items = new ArrayList<>();
                        for(int i=0; i<list.length(); i++) {
                            JSONObject tmp = list.getJSONObject(i).getJSONObject("circle");
                            int wid = tmp.getInt("wcid");

                            String query = "select * from circle_info where wid=" + wid + ";";

                            Cursor cur = mDataBase.rawQuery(query, null);
                            cur.moveToFirst();

                            //iterate query add items to dialog
                            if (cur.getCount() != 0) {
                                while (true) {
                                    try {
                                        items.add(new circle_instance(wid, cur.getString(cur.getColumnIndex("Name")),
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
                            }
                        }
                        adapter = new circle_info_adapter(items);
                        setUpRecyclerView();

                        viewGroup.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    textView.setText("Error! Retry..");
                    show_favorite();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(call.isCanceled()) {
                    Log.e("exploit", "Request cancelled..");
                } else {
                    Log.e("exploit", "Favorite failed...");
                    textView.setText("Error! Retry..");
                    show_favorite();
                }
            }
        });

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
