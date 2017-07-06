package com.example.mahmoudheshmat.musicapp;

import android.content.Context;
import android.media.MediaPlayer;
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
import android.widget.Toast;

import java.util.ArrayList;

public class notesAdapter  extends RecyclerView.Adapter<notesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<note_items> items;
    private MediaPlayer mp;
    Utilities utilities;
    SqltieDatabase sql;


    public notesAdapter(Context context, ArrayList<note_items> items) {
        this.context = context;
        this.items = items;
        utilities = new Utilities();
        mp = new MediaPlayer();
    }

    @Override
    public notesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_notes, parent, false);
        return new notesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(notesAdapter.ViewHolder holder, final int position) {

        final int note_id = items.get(position).getNote_id();
        final String note_title = items.get(position).getNote_title();
        final String note_date = items.get(position).getNote_date();

        holder.note_title.setText(note_title);
        holder.note_date.setText(note_date);

        adapterListener(holder, position, note_id);

    }

    private void adapterListener(notesAdapter.ViewHolder holder, final int position, final int note_id) {
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesDetailsFragment note = new notesDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("note_id", note_id);
                bundle.putString("note_title", items.get(position).getNote_title());
                bundle.putString("note_date", items.get(position).getNote_date());
                note.setArguments(bundle);
                addCenterFragments(note, Constant.NOTE);
            }
        });

        holder.more_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.delete_note:
                                sql = new SqltieDatabase(context);
                                items.remove(position);
                                notifyItemRemoved(position);
                                sql.deleteNote(note_id);
                                return true;
                            case R.id.search_by_content:

                                return true;
                        }

                        return false;
                    }
                });
                popup.inflate(R.menu.pop_up_notes);
                popup.show();
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

        ImageView more_image;
        TextView note_title;
        TextView note_date;
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
            more_image = (ImageView) itemView.findViewById(R.id.more);
            note_title = (TextView) itemView.findViewById(R.id.note_title);
            note_date = (TextView) itemView.findViewById(R.id.note_date);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    private void addCenterFragments(Fragment fragment, String tag) {
        FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}