package com.example.mahmoudheshmat.musicapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class fileManagerFragment  extends android.support.v4.app.Fragment{

    RecyclerView recyclerView;
    private fileManagerAadapter adapter;
    private ArrayList<type_file> data_list;
    LinearLayout folder_back;
    LinearLayoutManager linearLayoutManager;

    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_filemanager, container, false);

        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        if (toolbar != null) {
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        }
        toolbar.setTitle(R.string.folder);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.container)).commit();
            }
        });

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_viewFragment);
        folder_back = (LinearLayout)  rootView.findViewById(R.id.folder_back);

        data_list = FileLoader.getAllDirectoriesFromSDCard();

        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new fileManagerAadapter(getContext(), data_list, folder_back);
        recyclerView.setAdapter(adapter);

        return rootView;
    }



}