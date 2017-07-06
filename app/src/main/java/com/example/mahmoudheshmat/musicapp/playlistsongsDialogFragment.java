package com.example.mahmoudheshmat.musicapp;


import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class playlistsongsDialogFragment extends DialogFragment implements View.OnClickListener{

    RecyclerView recyclerView;
    private playlistaddSongsAdapter adapter;
    private ArrayList<playlist_items> data_list;
    LinearLayoutManager linearLayoutManager;
    TextView create_new_playlist;

    static String song_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addplaylistsong, null);
        getDialog().setTitle(R.string.add_playlist);
        setCancelable(true);

        linearLayoutManager = new LinearLayoutManager(view.getContext());
        create_new_playlist = (TextView) view.findViewById(R.id.new_playlist);
        create_new_playlist.setOnClickListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_viewFragment);

        Bundle bundle = getArguments();
        if(bundle != null){
            song_id = bundle.getString("pos");
        }

        data_list = new ArrayList<>();

        SqltieDatabase sql = new SqltieDatabase(view.getContext());

        data_list = sql.getAllPlaylists();

        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new playlistaddSongsAdapter(view.getContext(), data_list, song_id, playlistsongsDialogFragment.this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == create_new_playlist){
            NavigationUtils.navigatoPlaylists(getActivity());
        }
    }
}