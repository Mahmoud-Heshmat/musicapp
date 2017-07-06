package com.example.mahmoudheshmat.musicapp;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class songsAdapter extends RecyclerView.Adapter<songsAdapter.ViewHolder>{

    private Context context;
    private ArrayList<songs_item> items;
    List<Fragment> activeCenterFragments = new ArrayList<Fragment>();


    public songsAdapter(Context context, ArrayList<songs_item> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public songsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_songs, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(songsAdapter.ViewHolder holder, final int position) {

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



    private void setOnPopupMenuListener(final ViewHolder holder, final int position){
        holder.more_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.popup_song_play:
                                NavigationUtils.play_playingsongActivity(context, position);
                                return true;
                            case R.id.popup_song_play_next:

                                return true;
                            case R.id.popup_song_addto_queue:
                                NavigationUtils.queue_playingsongActivity(context, position);
                                return true;

                            case R.id.popup_song_addto_playlist:
                                addDialogFragment(items.get(position).getSong_id());
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
                                Utilities.shareFun(context, Long.valueOf(items.get(position).getSong_id()));
                                return true;

                            case R.id.popup_song_delete:

                                long[] deleteIds = {Long.valueOf(items.get(position).getSong_id())};
                                Utilities.showDeleteDialog(context, items.get(position).getSong_name(), deleteIds,
                                        songsAdapter.this, position);
                                return true;

                            case R.id.popup_song_favourite:
                                Boolean found = false;
                                SqltieDatabase sql = new SqltieDatabase(context);
                                ArrayList songsId = sql.getAllFavourite();
                                for (int i =0 ; i<songsId.size() ; i++){
                                    if(songsId.get(i).toString().equals(items.get(position).getSong_id())){
                                        found = true;
                                    }
                                }
                                if(found){
                                    Toast.makeText(context, "Songs is alerady added on favourites Before", Toast.LENGTH_LONG).show();
                                }else{
                                    sql.InsertRowSong(Integer.valueOf(items.get(position).getSong_id()));
                                    favouritesFragment favourites = new favouritesFragment();
                                    addCenterFragments(favourites, Constant.FAVOURITES);
                                }

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
                NavigationUtils.bottom_playingsongService(context, position);
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

    public void notifyDatachanged(ArrayList<songs_item> newitems){
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

    private void addCenterFragments(Fragment fragment, String tag) {
        FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void addDialogFragment(String song_id) {
        android.app.FragmentManager manager = ((FragmentActivity)context).getFragmentManager();
        playlistsongsDialogFragment playlist = new playlistsongsDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("pos", song_id);
        bundle.putParcelableArrayList("songs", items);
        playlist.setArguments(bundle);
        playlist.show(manager, "playlist");
    }
}