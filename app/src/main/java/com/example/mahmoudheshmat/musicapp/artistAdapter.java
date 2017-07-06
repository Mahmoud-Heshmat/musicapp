package com.example.mahmoudheshmat.musicapp;


import android.content.Context;
import android.content.Intent;
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

public class artistAdapter extends RecyclerView.Adapter<artistAdapter.ViewHolder> {

    private Context context;
    private List<songs_item> items;


    public artistAdapter(Context context, List<songs_item> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public artistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_artist, parent, false);
        return new artistAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(artistAdapter.ViewHolder holder, final int position) {

        String image_item = items.get(position).getSong_image();
        String artist_item = items.get(position).getArtist_name();

        Picasso.with(context)
                .load(image_item)
                .placeholder(R.drawable.placeholder)
                .into(holder.album_image);

        holder.artist_name.setText(artist_item);

        adapterListener(holder, position);

    }

    private void adapterListener(final artistAdapter.ViewHolder holder, final int position) {
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                artistSongsFragment play = new artistSongsFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("artistId", items.get(position).getArtist_id());
                bundle.putString("image", items.get(position).getSong_image());
                play.setArguments(bundle);

                addCenterFragments(play, Constant.artistSongsFragment);
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
        TextView artist_name;
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
            album_image = (ImageView) itemView.findViewById(R.id.album_image);
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