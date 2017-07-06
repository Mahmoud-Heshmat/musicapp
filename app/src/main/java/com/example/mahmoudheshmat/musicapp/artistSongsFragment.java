package com.example.mahmoudheshmat.musicapp;


import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class artistSongsFragment extends android.support.v4.app.Fragment {

    RecyclerView recyclerView;
    private artistSongsAdapter adapter;
    private ArrayList<songs_item> data_list;
    LinearLayoutManager linearLayoutManager;

    ImageView artist_image;

    int artistId;
    String image;
    String album = null;

    Toolbar mToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_abumsongs, container, false);

        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        if (mToolbar != null) {
            ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        }

        mToolbar.setTitle(getString(R.string.artist));
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.container)).commit();
            }
        });

        Bundle bundle = getArguments();
        if (bundle != null) {

            artistId = bundle.getInt("artistId");
            image = bundle.getString("image");

            artist_image = (ImageView) rootView.findViewById(R.id.song_image);
            Picasso.with(getContext())
                    .load(image)
                    .placeholder(R.drawable.placeholder)
                    .into(artist_image);

            linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_viewFragment);

            data_list = new ArrayList<>();

            getSongs get = new getSongs();

            data_list = get.getAllArtistSongs(getContext(), artistId);

            recyclerView.setLayoutManager(linearLayoutManager);
            adapter = new artistSongsAdapter(getContext(), data_list);
            recyclerView.setAdapter(adapter);
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onActivityCreated(savedInstanceState);
    }
}

