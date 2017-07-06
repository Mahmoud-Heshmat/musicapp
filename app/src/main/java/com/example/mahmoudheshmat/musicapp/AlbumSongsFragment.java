package com.example.mahmoudheshmat.musicapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class AlbumSongsFragment extends android.support.v4.app.Fragment{

    RecyclerView recyclerView;
    private albumSongsAdapter adapter;
    private ArrayList<songs_item> data_list;
    LinearLayoutManager linearLayoutManager;

    ImageView album_image;

    int albumId;
    String image;
    String albumName;

    Toolbar mToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_abumsongs, container, false);
        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        if (mToolbar != null) {
            ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        }
        mToolbar.setTitle(getString(R.string.album));
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.container)).commit();
            }
        });

        Bundle bundle = getArguments();
        if (bundle != null) {

            albumId = bundle.getInt("albumId");
            image = bundle.getString("image");
            albumName = bundle.getString("album");
            //Log.d("response" , albumName);

            album_image = (ImageView) rootView.findViewById(R.id.song_image);
            Picasso.with(getContext())
                    .load(image)
                    .placeholder(R.drawable.placeholder)
                    .into(album_image);

            linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_viewFragment);

            data_list = new ArrayList<>();

            getSongs get = new getSongs();

            data_list = get.getAllAlbumSsongs(getContext(), albumId);

            recyclerView.setLayoutManager(linearLayoutManager);
            adapter = new albumSongsAdapter(getContext(), data_list);
            recyclerView.setAdapter(adapter);
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

}