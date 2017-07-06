package com.example.mahmoudheshmat.musicapp;


import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class SongsFragment extends android.support.v4.app.Fragment {

    RecyclerView recyclerView;
    private songsAdapter adapter;
    private ArrayList<songs_item> data_list;
    LinearLayoutManager linearLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_songs, container, false);

        linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_viewFragment);

        data_list = getSongs.songs_data_list;

        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new songsAdapter(getContext(), data_list);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.song_sort_by, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        getSongs get_order = new getSongs();

        switch (item.getItemId()){
            case R.id.menu_sort_by_az:

                data_list.clear();
                data_list = get_order.song_sortby(getContext(), Constant.ASC_SORT, Constant.MEDIASTORE_TITLE);
                adapter.notifyDatachanged(data_list);

                return true;
            case  R.id.menu_sort_by_za:

                data_list.clear();
                data_list = get_order.song_sortby(getContext(), Constant.DESC_SORT, Constant.MEDIASTORE_TITLE);
                adapter.notifyDatachanged(data_list);

                return true;
            case R.id.menu_sort_by_new_old:

                data_list.clear();
                data_list = get_order.song_sortby(getContext(), Constant.ASC_SORT, Constant.MEDIASTORE_DATE);
                adapter.notifyDatachanged(data_list);

                return true;

            case R.id.menu_sort_by_old_new:

                data_list.clear();
                data_list = get_order.song_sortby(getContext(), Constant.DESC_SORT, Constant.MEDIASTORE_DATE);
                adapter.notifyDatachanged(data_list);

                return true;

            case R.id.menu_sort_by_artist:

                data_list.clear();
                data_list = get_order.song_sortby(getContext(), Constant.ASC_SORT, Constant.MEDIASTORE_ARTIST);
                adapter.notifyDatachanged(data_list);

                return true;
            case R.id.menu_sort_by_album:

                data_list.clear();
                data_list = get_order.song_sortby(getContext(), Constant.ASC_SORT, Constant.MEDIASTORE_ALBUM);
                adapter.notifyDatachanged(data_list);

                return true;

            case R.id.menu_sort_by_duration:

                data_list.clear();
                data_list = get_order.song_sortby(getContext(), Constant.ASC_SORT, Constant.MEDIASTORE_DURATION);
                adapter.notifyDatachanged(data_list);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
