package com.example.mahmoudheshmat.musicapp;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class playingsongFragment extends android.support.v4.app.Fragment {

    ImageView song_image, previous, next, timer,shuffle, repeat, playPause;
    TextView duration_progress, duration, song_name ;
    SeekBar seekBar_duration, seekBar_sound;

    static String Song_image, Song_name, Artist_name, Song_data;
    static long Song_duraion;

    MediaPlayer mediaPlayer;
    Utilities utilities ;
    private Handler mHandler = new Handler();

    private boolean isShuffle = false;
    private boolean isRepeat = false;

    private AudioManager audioManager = null;

    ArrayList<songs_item> items;
    private int currentSongIndex = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_playing_song, container, false);

        song_image = (ImageView) rootView.findViewById(R.id.song_image);
        previous = (ImageView) rootView.findViewById(R.id.previous);
        next = (ImageView) rootView.findViewById(R.id.next);
        repeat = (ImageView) rootView.findViewById(R.id.repeat);
        shuffle = (ImageView) rootView.findViewById(R.id.shuffle);
        timer = (ImageView) rootView.findViewById(R.id.timer);
        playPause = (ImageView) rootView.findViewById(R.id.playPause);

        duration_progress = (TextView) rootView.findViewById(R.id.duration_progress);
        duration = (TextView) rootView.findViewById(R.id.duration);
        song_name = (TextView) rootView.findViewById(R.id.song_name);

        seekBar_duration = (SeekBar) rootView.findViewById(R.id.seekBar_duration);
        seekBar_sound = (SeekBar) rootView.findViewById(R.id.seekBar_sound);


        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        mediaPlayer = new MediaPlayer();


        utilities = new Utilities();

        Bundle bundle = getArguments();
        if(bundle != null){
            items = bundle.getParcelableArrayList("songs");
            currentSongIndex = bundle.getInt("pos");
            play(currentSongIndex);
            Log.d("response",String.valueOf( currentSongIndex));
        }

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        seekBar_sound.setMax(audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekBar_sound.setProgress(audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC));


        seekBar_duration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = utilities.progressToTimer(seekBar.getProgress(), totalDuration);

                // forward or backward to certain seconds
                mediaPlayer.seekTo(currentPosition);

                // update timer progress again
                updateProgressBar();
            }
        });
        seekBar_sound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previous();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repeat();
            }
        });

        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuffle();
            }
        });


        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    playPause.setImageResource(R.drawable.play_music);
                    mediaPlayer.pause();
                }else{
                    playPause.setImageResource(R.drawable.pause_music);
                    mediaPlayer.start();
                }

            }
        });


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                onComplete();
            }
        });

    }

    private void play(int CurrentSongIndex){

        playPause.setImageResource(R.drawable.pause_music);

        Song_image = items.get(CurrentSongIndex).getSong_image();
        Song_name = items.get(CurrentSongIndex).getSong_name();
        Artist_name = items.get(CurrentSongIndex).getArtist_name();
        Song_duraion = items.get(CurrentSongIndex).getSong_duarion();
        Song_data = items.get(CurrentSongIndex).getSong_data();

        Log.d("response", String.valueOf(Song_duraion)+"  "+ Artist_name+"  "+ Song_data);

        Picasso.with(getContext())
                .load(Song_image)
                .placeholder(R.drawable.placeholder)
                .into(song_image);

        this.song_name.setText(Song_name + "  " + Artist_name);

        String Duration = utilities.milliSecondsToTimer(Song_duraion);


        this.duration.setText(Duration);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(Song_data);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                    // set Progress bar values
                    seekBar_duration.setProgress(0);
                    seekBar_duration.setMax(100);

                    // Updating progress bar
                    updateProgressBar();
                } catch (IllegalArgumentException | IllegalStateException | IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void repeat(){
        if(isRepeat){
            isRepeat = false;
            Toast.makeText(getContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
            repeat.setImageResource(R.drawable.repeat);
        }else{
            // make repeat to true
            isRepeat = true;
            Toast.makeText(getContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
            // make shuffle to false
            isShuffle = false;
            repeat.setImageResource(R.drawable.repeat_focus);
            shuffle.setImageResource(R.drawable.shuffle);
        }
    }

    private void shuffle(){
        if(isShuffle){
            isShuffle = false;
            Toast.makeText(getContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
            shuffle.setImageResource(R.drawable.shuffle);
        }else{
            // make repeat to true
            isShuffle= true;
            Toast.makeText(getContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
            // make shuffle to false
            isRepeat = false;
            shuffle.setImageResource(R.drawable.shuffle_focus);
            repeat.setImageResource(R.drawable.repeat);
        }
    }

    private void previous(){
        if(currentSongIndex > 0){
            play(currentSongIndex - 1);
            currentSongIndex = currentSongIndex - 1;
        }else{
            // play last song
            play(items.size() - 1);
            currentSongIndex = items.size() - 1;
        }
    }

    private void next(){
        if(currentSongIndex < (items.size() - 1)){
            play(currentSongIndex + 1);
            currentSongIndex = currentSongIndex + 1;
        }else{
            // play first song
            play(0);
            currentSongIndex = 0;
        }
    }

    private void onComplete(){
        if (isRepeat) {
            // repeat is on play same song again
            play(currentSongIndex);
        } else if (isShuffle) {
            // shuffle is on - play a random song
            Random rand = new Random();
            currentSongIndex = rand.nextInt((items.size() - 1) - 0 + 1) + 0;
            play(currentSongIndex);
        } else {
            // no repeat or shuffle ON - play next song
            if (currentSongIndex < (items.size() - 1)) {
                play(currentSongIndex + 1);
                currentSongIndex = currentSongIndex + 1;
            } else {
                // play first song
                play(0);
                currentSongIndex = 0;
            }
        }
    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }


    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();

            // Displaying time completed playing
            duration_progress.setText("" + utilities.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int)(utilities.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            seekBar_duration.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        mediaPlayer.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
