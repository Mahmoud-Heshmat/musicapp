package com.example.mahmoudheshmat.musicapp;


import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ArtistsFragment extends android.support.v4.app.Fragment {

    RecyclerView recyclerView;
    private artistAdapter adapter;
    private List<songs_item> data_list;
    GridLayoutManager gridLayoutManager;

    SharedPreferences sharedPreferences;
    String sort_type;
    String sort_by;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getContext().getSharedPreferences(Constant.SORT_ARTIST, Context.MODE_PRIVATE);
        sort_type = sharedPreferences.getString(Constant.SORT_TYPE, Constant.ASC_SORT);
        sort_by = sharedPreferences.getString(Constant.SORT_BY, Constant.MEDIASTORE_TITLE_ALBUM);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_artist, container, false);

        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_viewFragment);

        data_list = new ArrayList<>();

        getSongs get = new getSongs();
        data_list = get.getAllArtists(getContext(), sort_type, sort_by);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new artistAdapter(getContext(), data_list);
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
        inflater.inflate(R.menu.artist_sort_by, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        getSongs get_order = new getSongs();

        switch (item.getItemId()){
            case R.id.menu_sort_by_az:

                data_list.clear();
                data_list = get_order.artist_sortby(getContext(), Constant.ASC_SORT, Constant.MEDIASTORE_TITLE_ARTIST);
                adapter.notifyDatachanged(data_list);
                return true;
            case R.id.menu_sort_by_za:

                data_list.clear();
                data_list = get_order.artist_sortby(getContext(), Constant.DESC_SORT, Constant.MEDIASTORE_TITLE_ARTIST);
                adapter.notifyDatachanged(data_list);

                return true;
            case R.id.menu_sort_by_number_of_songs:

                data_list.clear();
                data_list = get_order.artist_sortby(getContext(), Constant.ASC_SORT, Constant.MEDIASTORE_NUMBER_OF_TRACKS_ARTIST);
                adapter.notifyDatachanged(data_list);

                return true;
            case R.id.menu_sort_by_number_of_albums:

                data_list.clear();
                data_list = get_order.artist_sortby(getContext(), Constant.ASC_SORT, Constant.MEDIASTORE_NUMBER_OF_ALBUMS_ARTIST);
                adapter.notifyDatachanged(data_list);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
