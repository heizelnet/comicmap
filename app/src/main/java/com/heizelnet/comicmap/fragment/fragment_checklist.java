package com.heizelnet.comicmap.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.heizelnet.comicmap.CheckListAdapter;
import com.heizelnet.comicmap.MyApplication;
import com.heizelnet.comicmap.R;
import com.heizelnet.comicmap.SwipeDeleter;


public class fragment_checklist extends Fragment {

    private ViewGroup viewGroup;
    private RecyclerView recyclerView;
    private EditText editText;
    private CheckListAdapter mAdapter;

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
        //cache = pref.getStringArrayList(pref.CHECKLIST_KEY);
        mAdapter = new CheckListAdapter();

        //Test Code for update
        setUpRecyclerView();
        if(mAdapter.getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            viewGroup.setVisibility(View.VISIBLE);
        } else {
            viewGroup.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        editText = view.findViewById(R.id.editText_chklist);
        editText.setOnEditorActionListener((v, actionId, event) -> {
            switch(actionId) {
                case EditorInfo.IME_ACTION_DONE:
                    // 완료 버튼
                    if(mAdapter.getItemCount() == 0) {
                        //cache.add(editText.getText().toString());
                        //mAdapter.notifyItemInserted(mAdapter.getItemCount());
                        mAdapter.addItem(mAdapter.getItemCount(), editText.getText().toString());
                        viewGroup.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        //cache.add(editText.getText().toString());
                        //mAdapter.notifyItemInserted(mAdapter.getItemCount());
                        mAdapter.addItem(mAdapter.getItemCount(), editText.getText().toString());
                    }
                    editText.getText().clear();
                    break;
            }
            return false;
        });

        return view;
    }

    private void setUpRecyclerView() {
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyApplication.getAppContext()));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeDeleter(mAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
