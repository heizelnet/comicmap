package com.example.comicmap;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class fragment_favorite extends Fragment {

    private ViewGroup viewGroup;
    private RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        viewGroup = view.findViewById(R.id.empty_group);
        recyclerView = view.findViewById(R.id.recview);

        //Check favorite Database
        SharedPreferences prfs = getActivity().getSharedPreferences("Favorite", Context.MODE_PRIVATE);
        if(prfs.getInt("Counter", 0) == 0) {
            recyclerView.setVisibility(View.GONE);
            viewGroup.setVisibility(View.VISIBLE);
        } else {
            viewGroup.setVisibility(View.GONE);
        }

        //Json to DataArray -> show RecyclerView
        return view;
    }
}
