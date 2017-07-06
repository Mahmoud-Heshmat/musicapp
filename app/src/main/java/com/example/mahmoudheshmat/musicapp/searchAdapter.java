package com.example.mahmoudheshmat.musicapp;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import java.util.Collections;
import java.util.List;

public class searchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List songResults = Collections.emptyList();

    SearchView searchView;

    private List<Object> searchResults =  new ArrayList<>();

    public searchAdapter(Context context, SearchView searchView) {
        this.context = context;
        this.searchView = searchView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType){
            case Song_type.SONG:
                return new songHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_song_search, parent, false));
            case Song_type.ALBUM:
                return new albumHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_album_search, parent, false));
            case Song_type.ARTIST:
                return new artistHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_artist_search, parent, false));
            case Song_type.HEADER:
                return new headerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_section_header, parent, false));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        switch (getItemViewType(position)){
            case Song_type.SONG:
                songHolder song = (songHolder) holder;
                type_song song_list = (type_song) searchResults.get(position);

                String data = song_list.getSong_data();

                Picasso.with(context)
                        .load(song_list.getSong_image())
                        .placeholder(R.drawable.placeholder)
                        .into(song.song_image);

                song.song_name.setText(song_list.getSong_name());
                song.artist_name.setText(song_list.getArtist_name());

                setSongsOnPopupMenuListener(song, position, data);
                break;
            case Song_type.ALBUM:

                albumHolder album = (albumHolder) holder;
                type_album album_list = (type_album) searchResults.get(position);
                Picasso.with(context)
                        .load(album_list.getSong_image())
                        .placeholder(R.drawable.placeholder)
                        .into(album.album_image);

                album.artist_name.setText(album_list.getArtist_name());
                album.album_name.setText(album_list.getSong_name());
                setAlbumsOnPopupMenuListener(album, album_list.getAlbum_id(), album_list.getSong_image(), album_list.getSong_name());
                break;
            case Song_type.ARTIST:
                artistHolder artist = (artistHolder) holder;
                type_artist artist_list = (type_artist) searchResults.get(position);
                Picasso.with(context)
                        .load(artist_list.getSong_image())
                        .placeholder(R.drawable.placeholder)
                        .into(artist.album_image);

                artist.artist_name.setText(artist_list.getArtist_name());
                break;
            case Song_type.HEADER:
                headerHolder headers = (headerHolder) holder;
                headers.header.setText((String) searchResults.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemViewType(int position) {

        if (searchResults.get(position) instanceof type_song)
            return Song_type.SONG;
        else if (searchResults.get(position) instanceof type_album)
            return Song_type.ALBUM;
        else if (searchResults.get(position) instanceof type_artist)
            return Song_type.ARTIST;
        else if (searchResults.get(position) instanceof String)
            return Song_type.HEADER;

        return super.getItemViewType(position);
    }

    public void updateSearchResults( List songs , List albums, List artists) {

        List list = new ArrayList<>();
        if(!songs.isEmpty()){
            list.add(context.getString(R.string.songs));
            list.addAll(songs);
        }
        if(!albums.isEmpty()){
            list.add(context.getString(R.string.album));
            list.addAll(albums);
        }
        if(!artists.isEmpty()){
            list.add(context.getString(R.string.artist));
            list.addAll(artists);
        }

        if(list != null){
            this.searchResults = list;
            notifyDataSetChanged();
        }
    }

    public void updateSearchResultsF(){
        searchResults.clear();
        notifyDataSetChanged();
    }

    private void setSongsOnPopupMenuListener(final songHolder holder, final int position, final String data){

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtils.play_songActivity_search(context, position-1);
                //Log.d("response", String.valueOf(position));
            }
        });

    }

    private void setAlbumsOnPopupMenuListener(albumHolder holder, final int album_id, final String image_item, final String album_name) {
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlbumSongsFragment play = new AlbumSongsFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("albumId", album_id);
                bundle.putString("image", image_item);
                bundle.putString("album", album_name);
                play.setArguments(bundle);
                addCenterFragments(play, Constant.albumSongsFragment);
            }
        });
    }

    private void addCenterFragments(Fragment fragment, String tag) {
        FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // songs holder
    class songHolder extends RecyclerView.ViewHolder {
        ImageView song_image;
        TextView song_name;
        TextView artist_name;
        CardView cardView;

        songHolder(View itemView) {
            super(itemView);
            song_image = (ImageView) itemView.findViewById(R.id.song_image);
            song_name = (TextView) itemView.findViewById(R.id.song_name);
            artist_name = (TextView) itemView.findViewById(R.id.artist_name);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    // album holder
    class albumHolder extends RecyclerView.ViewHolder {
        ImageView album_image;
        TextView album_name;
        TextView artist_name;
        CardView cardView;

        albumHolder(View itemView) {
            super(itemView);
            album_image = (ImageView) itemView.findViewById(R.id.album_image);
            album_name = (TextView) itemView.findViewById(R.id.album_name);
            artist_name = (TextView) itemView.findViewById(R.id.artist_name);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    // artist holder
    class artistHolder extends RecyclerView.ViewHolder {
        ImageView album_image;
        TextView artist_name;
        CardView cardView;

        artistHolder(View itemView) {
            super(itemView);
            album_image = (ImageView) itemView.findViewById(R.id.album_image);
            artist_name = (TextView) itemView.findViewById(R.id.artist_name);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    // header holder
    class headerHolder extends RecyclerView.ViewHolder {

        TextView header;

        headerHolder(View itemView) {
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.section_header);
        }
    }

}