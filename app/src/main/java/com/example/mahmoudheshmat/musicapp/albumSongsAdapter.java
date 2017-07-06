package com.example.mahmoudheshmat.musicapp;


import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class albumSongsAdapter  extends RecyclerView.Adapter<albumSongsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<songs_item> items;
    private MediaPlayer mp;

    Utilities utilities;


    public albumSongsAdapter(Context context, ArrayList<songs_item> items) {
        this.context = context;
        this.items = items;
        utilities = new Utilities();
        mp = new MediaPlayer();
    }

    @Override
    public albumSongsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_albumsongs, parent, false);
        return new albumSongsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(albumSongsAdapter.ViewHolder holder, final int position) {

        final String image_item = items.get(position).getSong_image();
        final String name_item = items.get(position).getSong_name();
        final String artist_item = items.get(position).getArtist_name();
        final long duartion = items.get(position).getSong_duarion();
        final String song_data = items.get(position).getSong_data();

        Picasso.with(context)
                .load(image_item)
                .placeholder(R.drawable.placeholder)
                .into(holder.song_image);


        String song_duration = utilities.milliSecondsToTimer(duartion);

        holder.song_name.setText(name_item);
        holder.song_duration.setText(song_duration);

        adapterListener(holder, position);

    }

    private void adapterListener(ViewHolder holder,final int position) {
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtils.album_playingsongActivity(context, position);
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

        ImageView song_image;
        ImageView more_image;
        TextView song_name;
        TextView song_duration;
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
            song_image = (ImageView) itemView.findViewById(R.id.song_image);
            more_image = (ImageView) itemView.findViewById(R.id.more);
            song_name = (TextView) itemView.findViewById(R.id.song_name);
            song_duration = (TextView) itemView.findViewById(R.id.song_duration);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }
}