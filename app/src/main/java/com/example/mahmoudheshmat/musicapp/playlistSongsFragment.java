package com.example.mahmoudheshmat.musicapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class playlistSongsFragment extends android.support.v4.app.Fragment implements View.OnClickListener{

    RecyclerView recyclerView;
    private playlistSongsAdapter adapter;
    private ArrayList<songs_item> data_list;
    LinearLayoutManager linearLayoutManager;
    FloatingActionButton fab;

    Thread thread;
    int playlist_id;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_playlistsongs, container, false);

        Bundle bundle = getArguments();
        if(bundle != null){
            playlist_id = bundle.getInt("playlist_id");
        }

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_viewFragment);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        data_list = new ArrayList<>();

        SqltieDatabase sql = new SqltieDatabase(getContext());
        ArrayList<playlist_items> arrayList = sql.getPlaylistSongs(playlist_id);

        getSongs get = new getSongs();
        data_list = get.Playlist(getContext(), arrayList);

        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new playlistSongsAdapter(getContext(), data_list);
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

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v == fab){
            NavigationUtils.navigateSongs(v.getContext());
        }
    }
}
