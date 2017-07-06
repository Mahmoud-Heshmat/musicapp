package com.example.mahmoudheshmat.musicapp;


import android.content.Context;
import android.content.Intent;
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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class albumsAdapter  extends RecyclerView.Adapter<albumsAdapter.ViewHolder> {

    private Context context;
    private List<songs_item> items;


    public albumsAdapter(Context context, List<songs_item> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public albumsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_album, parent, false);
        return new albumsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(albumsAdapter.ViewHolder holder, final int position) {

        String image_item = items.get(position).getSong_image();
        String album_item = items.get(position).getSong_name();
        String artist_item = items.get(position).getArtist_name();
        int album_id = items.get(position).getAlbum_id();

        Picasso.with(context)
                .load(image_item)
                .placeholder(R.drawable.placeholder)
                .into(holder.album_image);

        holder.artist_name.setText(artist_item);
        holder.album_name.setText(album_item);

        adapterListener(holder, album_id, image_item);

    }

    private void adapterListener(albumsAdapter.ViewHolder holder, final int album_id, final String image_item) {
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlbumSongsFragment play = new AlbumSongsFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("albumId", album_id);
                bundle.putString("image", image_item);
                play.setArguments(bundle);
                addCenterFragments(play, Constant.albumSongsFragment);
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

    public void notifyDatachanged(List<songs_item> newitems){
        items.clear();
        items = newitems;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView album_image;
        TextView album_name;
        TextView artist_name;
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
            album_image = (ImageView) itemView.findViewById(R.id.album_image);
            album_name = (TextView) itemView.findViewById(R.id.album_name);
            artist_name = (TextView) itemView.findViewById(R.id.artist_name);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    private void addCenterFragments(Fragment fragment, String tag) {
        FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}