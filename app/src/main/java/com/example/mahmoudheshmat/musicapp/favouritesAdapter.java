package com.example.mahmoudheshmat.musicapp;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class favouritesAdapter extends RecyclerView.Adapter<favouritesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<songs_item> items;
    List<Fragment> activeCenterFragments = new ArrayList<Fragment>();


    public favouritesAdapter(Context context, ArrayList<songs_item> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public favouritesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_favourites, parent, false);
        return new favouritesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(favouritesAdapter.ViewHolder holder, final int position) {

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

    private void setOnPopupMenuListener(final favouritesAdapter.ViewHolder holder, final int position) {
        holder.more_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.popup_song_play:
                                NavigationUtils.favourite_playingsongActivity(context, position);
                                return true;

                            case R.id.popup_song_goto_album:
                                AlbumSongsFragment play = new AlbumSongsFragment();
                                Bundle bundle = new Bundle();
                                bundle.putInt("albumId", items.get(position).getAlbum_id());
                                bundle.putString("image", items.get(position).getSong_image());
                                play.setArguments(bundle);
                                addCenterFragments(play, Constant.albumSongsFragment);
                                return true;

                            case R.id.popup_song_goto_artist:
                                artistSongsFragment artist = new artistSongsFragment();
                                Bundle bundle2 = new Bundle();
                                bundle2.putInt("artistId", items.get(position).getArtist_id());
                                bundle2.putString("image", items.get(position).getSong_image());
                                artist.setArguments(bundle2);
                                addCenterFragments(artist, Constant.artistSongsFragment);

                                return true;
                            case R.id.popup_song_share:

                                return true;

                            case R.id.popup_song_delete:
                                SqltieDatabase sql = new SqltieDatabase(context);
                                sql.deleteFavourite(Integer.valueOf(items.get(position).getSong_id()));
                                items.remove(position);
                                notifyItemRemoved(position);
                                return true;
                        }

                        return false;
                    }
                });
                popup.inflate(R.menu.pop_up_favourites);
                popup.show();
            }
        });


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtils.favourite_playingsongActivity(context, position);
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

    private void addCenterFragments(Fragment fragment, String tag) {
        FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}