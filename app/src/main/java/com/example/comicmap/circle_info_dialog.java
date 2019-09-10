package com.example.comicmap;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class circle_info_dialog extends Dialog {


    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter adapter;
    private Fragment fragment;
    private RecyclerView recyclerView;

    public circle_info_dialog(@NonNull Context context) {
        super(context);
    }
    public circle_info_dialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public circle_info_dialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public circle_info_dialog(Fragment fragment, RecyclerView.Adapter adapter) {
        super(fragment.getContext());
        this.fragment = fragment;
        this.adapter = adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.circle_info_dialog);
        recyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(fragment.getContext());
        recyclerView.setLayoutManager(mLayoutManager);


        recyclerView.setAdapter(adapter);

    }

}
