package com.example.mahmoudheshmat.musicapp;


import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class favouritesFragment extends android.support.v4.app.Fragment  {

    RecyclerView recyclerView;
    private favouritesAdapter adapter;
    private ArrayList<songs_item> data_list;
    LinearLayoutManager linearLayoutManager;

    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_favourites, container, false);

        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        if (toolbar != null) {
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        }
        toolbar.setTitle(R.string.favourite);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.container)).commit();
            }
        });

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_viewFragment);

        data_list = new ArrayList<>();

        getSongs get = new getSongs();

        SqltieDatabase sql = new SqltieDatabase(getContext());
        ArrayList arrayList = sql.getAllFavourite();

        data_list = get.Favourites(getContext(), arrayList);

        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new favouritesAdapter(getContext(), data_list);
        recyclerView.setAdapter(adapter);

        return rootView;
    }
}
