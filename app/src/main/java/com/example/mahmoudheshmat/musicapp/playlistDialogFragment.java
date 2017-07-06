package com.example.mahmoudheshmat.musicapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class playlistDialogFragment extends DialogFragment implements View.OnClickListener{

    Communictor communictor;

    EditText playlist_name;
    Button btn_add;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.communictor = (Communictor)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement Communictor");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_playlist, null);
        getDialog().setTitle(R.string.add_playlist);
        setCancelable(true);

        playlist_name = (EditText) view.findViewById(R.id.playlist_name);
        btn_add = (Button) view.findViewById(R.id.add_playlist);
        btn_add.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        String playName = playlist_name.getText().toString();
        if(v == btn_add){
            if(playName.isEmpty()){
                Toast.makeText(v.getContext(), "You shouid add playist name", Toast.LENGTH_LONG).show();
            }else{
                SqltieDatabase sql = new SqltieDatabase(v.getContext());
                if(sql.checkPlaylist(playName)){
                    Toast.makeText(v.getContext(), "Playlist name is found before", Toast.LENGTH_LONG).show();
                }else{
                    communictor.DialogMessage(playName);
                    dismiss();
                }
            }
        }
    }

    public interface Communictor{
        void DialogMessage(String messgae);
    }

}
