package com.example.mahmoudheshmat.musicapp;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class playlistaddSongsAdapter  extends RecyclerView.Adapter<playlistaddSongsAdapter.ViewHolder> {

    private Context context;
    private List<playlist_items> items;
    private static String song_id;

    playlistsongsDialogFragment dailogFragment;


    public playlistaddSongsAdapter(Context context, List<playlist_items> items, String song_id, playlistsongsDialogFragment dailogFragment) {
        this.context = context;
        this.items = items;
        this.song_id = song_id;
        this.dailogFragment = dailogFragment;
    }

    @Override
    public playlistaddSongsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_addplaylist, parent, false);
        return new playlistaddSongsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(playlistaddSongsAdapter.ViewHolder holder, final int position) {

        String playlist_name = items.get(position).getPlaylist_name();

        holder.playlist_name.setText(playlist_name);

        adapterListener(holder, position);

    }


    private void adapterListener(final playlistaddSongsAdapter.ViewHolder holder, final int position) {
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SqltieDatabase sql = new SqltieDatabase(context);
                if(sql.checkisSongsinPlaylist(items.get(position).getPlaylist_id(), Long.valueOf(song_id))){

                    Toast.makeText(context, "Songs is added before in this playlist", Toast.LENGTH_LONG).show();
                }else {
                    sql.InsertRowPlaylistSongs(items.get(position).getPlaylist_id(), Long.valueOf(song_id));
                    Toast.makeText(context, "Added successfully is playlist", Toast.LENGTH_LONG).show();
                    dailogFragment.dismiss();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView playlist_image;
        TextView playlist_name;
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
            playlist_image = (ImageView) itemView.findViewById(R.id.playlist_image);
            playlist_name = (TextView) itemView.findViewById(R.id.playlist_name);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }
}