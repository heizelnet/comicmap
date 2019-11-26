package com.example.comicmap.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicmap.CheckListAdapter;
import com.example.comicmap.DataSharedPreference;
import com.example.comicmap.MyApplication;
import com.example.comicmap.R;
import com.example.comicmap.SwipeDeleter;

public class fragment_checklist extends Fragment {

    private ViewGroup viewGroup;
    private RecyclerView recyclerView;
    private DataSharedPreference pref = new DataSharedPreference();
    private CheckListAdapter mAdapter = new CheckListAdapter(pref.getStringArrayList(pref.CHECKLIST_KEY));

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
        View view = inflater.inflate(R.layout.fragment_checklist, container, false);


        viewGroup = view.findViewById(R.id.chk_empty_group);
        recyclerView = view.findViewById(R.id.rec_chk_view);

        //Test Code for update
        //setUpRecyclerView();

        if(mAdapter.getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            viewGroup.setVisibility(View.VISIBLE);
        } else {
            viewGroup.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private void setUpRecyclerView() {
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyApplication.getAppContext()));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeDeleter(mAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

}
