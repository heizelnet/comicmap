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
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicmap.DataBaseHelper;
import com.example.comicmap.LoginSharedPreference;
import com.example.comicmap.OAuth.APIClient;
import com.example.comicmap.OAuth.LoginClient;
import com.example.comicmap.OAuth.TokenProcess;
import com.example.comicmap.R;

import org.jetbrains.annotations.NotNull;

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

        textView.setText("Loading..");
        apiInterface = APIClient.getClient(TokenProcess.API_URL).create(TokenProcess.class);
        responseBodyCall = apiInterface.getFavoriteList("1");
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    Log.e("exploit", "Code : " + response.code());
                    Log.e("exploit", "Message : " + response.message());
                    textView.setText("Error!");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("exploit", "Call Failed...");
                textView.setText("Failed..");
            }
        });

        return view;
    }
}
