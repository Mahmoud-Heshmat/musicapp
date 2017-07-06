package com.example.mahmoudheshmat.musicapp;


import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class sourceFragment extends android.support.v4.app.Fragment implements View.OnClickListener{

    CardView youtube;
    CardView soundCloud;
    CardView web;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_source, container, false);

        youtube = (CardView) rootView.findViewById(R.id.youtubeCard);
        soundCloud = (CardView) rootView.findViewById(R.id.soundCard);
        web = (CardView) rootView.findViewById(R.id.webCard);

        youtube.setOnClickListener(this);
        soundCloud.setOnClickListener(this);
        web.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v == youtube){

        }else if (v == soundCloud){

        }else if (v == web){
            NavigationUtils.navigatoYoutube(getContext());
        }
    }
}
