package com.example.mahmoudheshmat.musicapp;

import android.app.FragmentManager;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class playlistActivity extends AppCompatActivity implements View.OnClickListener, playlistDialogFragment.Communictor{

    FloatingActionButton fab;

    Toolbar toolbar;
    Context context;

    RecyclerView recyclerView;
    private playlistAdapter adapter;
    private ArrayList<playlist_items> data_list;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.playlists));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        context = this;

        // Floating action button
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        //RecycleView
        gridLayoutManager = new GridLayoutManager(context, 2);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_viewFragment);

        data_list = new ArrayList<>();

        SqltieDatabase sql = new SqltieDatabase(context);

        data_list = sql.getAllPlaylists();

        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new playlistAdapter(context, data_list);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0)
                    fab.hide();
                else if (dy < 0)
                    fab.show();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @Override
    public void DialogMessage(String messgae) {
        SqltieDatabase sqls = new SqltieDatabase(context);
        sqls.InsertRowPlaylist(messgae);
        SqltieDatabase sql = new SqltieDatabase(context);
        adapter.notifyDatachanged(sql.getAllPlaylists());
    }

    @Override
    public void onClick(View v) {
        if(v == fab){
            addCenterFragments();
        }
    }

    private void addCenterFragments() {
        FragmentManager manager = getFragmentManager();
        playlistDialogFragment playlist = new playlistDialogFragment();
        playlist.show(manager, "playlist");
    }


}
