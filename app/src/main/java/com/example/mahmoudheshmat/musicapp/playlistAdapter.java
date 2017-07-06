package com.example.mahmoudheshmat.musicapp;


import android.content.Context;
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
import android.widget.Toast;


import java.util.List;

public class playlistAdapter extends RecyclerView.Adapter<playlistAdapter.ViewHolder> {

    private Context context;
    private List<playlist_items> items;


    public playlistAdapter(Context context, List<playlist_items> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public playlistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_playlist, parent, false);
        return new playlistAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(playlistAdapter.ViewHolder holder, final int position) {

        String playlist_name = items.get(position).getPlaylist_name();

        holder.playlist_name.setText(playlist_name);

        adapterListener(holder, position);

    }


    private void adapterListener(final playlistAdapter.ViewHolder holder, final int position) {
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playlistSongsFragment play = new playlistSongsFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("playlist_id", items.get(position).getPlaylist_id());
                play.setArguments(bundle);

                addCenterFragments(play, Constant.PLAYLIST);
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

    public void notifyDatachanged(List<playlist_items> newitems){
        items.clear();
        items = newitems;
        notifyDataSetChanged();
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

    private void addCenterFragments(Fragment fragment, String tag) {
        FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}