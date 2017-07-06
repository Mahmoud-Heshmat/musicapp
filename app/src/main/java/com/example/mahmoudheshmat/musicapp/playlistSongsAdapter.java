package com.example.mahmoudheshmat.musicapp;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class playlistSongsAdapter  extends RecyclerView.Adapter<playlistSongsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<songs_item> items;
    List<Fragment> activeCenterFragments = new ArrayList<Fragment>();


    public playlistSongsAdapter(Context context, ArrayList<songs_item> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public playlistSongsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_playlist_songs, parent, false);
        return new playlistSongsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(playlistSongsAdapter.ViewHolder holder, final int position) {

        final String image_item = items.get(position).getSong_image();
        final String name_item = items.get(position).getSong_name();
        final String artist_item = items.get(position).getArtist_name();

        Picasso.with(context)
                .load(image_item)
                .placeholder(R.drawable.placeholder)
                .into(holder.song_image);

        holder.song_name.setText(name_item);
        holder.artist_name.setText(artist_item);

        setOnPopupMenuListener(holder, position);
    }

    private void setOnPopupMenuListener(final playlistSongsAdapter.ViewHolder holder, final int position) {
        holder.more_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.popup_song_play:
                                NavigationUtils.play_playingsongActivity(context, position);
                                return true;
                            case R.id.popup_song_play_next:

                                return true;
                        }

                        return false;
                    }
                });
                popup.inflate(R.menu.pop_up_song);
                popup.show();
            }
        });


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtils.card_playlistActivity(context, position);
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

    public void notifyDatachanged(ArrayList<songs_item> newitems) {
        items.clear();
        items = newitems;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView song_image;
        ImageView more_image;
        TextView song_name;
        TextView artist_name;
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
            song_image = (ImageView) itemView.findViewById(R.id.song_image);
            more_image = (ImageView) itemView.findViewById(R.id.more);
            song_name = (TextView) itemView.findViewById(R.id.song_name);
            artist_name = (TextView) itemView.findViewById(R.id.artist_name);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }
}