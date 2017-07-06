package com.example.mahmoudheshmat.musicapp;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ikovac.timepickerwithseconds.MyTimePickerDialog;
import com.ikovac.timepickerwithseconds.TimePicker;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class playingsongActivity extends AppCompatActivity implements  SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    ImageView song_image, previous, timer,shuffle, repeat, next, playPause;
    TextView duration_progress, duration, song_name, countTimer ;
    SeekBar seekBar_duration, seekBar_sound;

    static String Song_image, Song_name, Artist_name, Song_data;
    static long Song_duraion;

    MediaPlayer mediaPlayer;
    Utilities utilities ;

    private static boolean isShuffle = false;
    private static boolean isRepeat = false;
    private static boolean isPlaying = false;

    private AudioManager audioManager = null;

    ArrayList<songs_item> items;
    private static int currentSongIndex = 0;

    Context context;

    LocalBroadcastManager bManager;
    IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playingsong);

        bManager = LocalBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.SONG_DATA);
        bManager.registerReceiver(Receiver, intentFilter);

        context = this;

        song_image = (ImageView) findViewById(R.id.song_image);
        previous = (ImageView) findViewById(R.id.previous);
        next = (ImageView) findViewById(R.id.next);
        repeat = (ImageView) findViewById(R.id.repeat);
        shuffle = (ImageView) findViewById(R.id.shuffle);
        timer = (ImageView) findViewById(R.id.timer);
        playPause = (ImageView) findViewById(R.id.playPause);


        duration_progress = (TextView) findViewById(R.id.duration_progress);
        duration = (TextView) findViewById(R.id.duration);
        song_name = (TextView) findViewById(R.id.song_name);
        countTimer = (TextView) findViewById(R.id.textTimer);

        seekBar_duration = (SeekBar) findViewById(R.id.seekBar_duration);
        seekBar_sound = (SeekBar) findViewById(R.id.seekBar_sound);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mediaPlayer = new MediaPlayer();
        seekBar_duration.setOnSeekBarChangeListener(this);

        utilities = new Utilities();

        check_isShuffle_isRepeat();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String type = bundle.getString("type");

            if (type.equals("artist")){
                isPlaying = true;
                items = getSongs.artistSongs_data_list;
                currentSongIndex = bundle.getInt("pos");
                NavigationUtils.artist_playingsongService(context, currentSongIndex);
                setSongData(currentSongIndex);

            }else if (type.equals("album")){
                isPlaying = true;
                items = getSongs.AlbumSongs_data_list;
                currentSongIndex = bundle.getInt("pos");
                NavigationUtils.album_playingsongService(context, currentSongIndex);
                setSongData(currentSongIndex);

            }else if (type.equals("favourite")){
                items = getSongs.favourites_data_list;
                currentSongIndex = bundle.getInt("pos");
                NavigationUtils.favourite_playingsongService(context, currentSongIndex);
                setSongData(currentSongIndex);

            }else if (type.equals("playlist")){
                items = getSongs.playlist_data_list;
                currentSongIndex = bundle.getInt("pos");
                NavigationUtils.playlist_playingsongService(context, currentSongIndex);
                setSongData(currentSongIndex);

            }else if (type.equals("play_search")){
                isPlaying = true;
                check_isShuffle_isRepeat();
                items = getSongs.search_song_result;
                currentSongIndex = bundle.getInt("pos");
                //currentSongIndex = currentSongIndex -1;
                setSongData(currentSongIndex);
                NavigationUtils.playingsongServiceSearch(context, currentSongIndex);

            }else if (type.equals("fromFile")){
                isPlaying = true;
                check_isShuffle_isRepeat();
                items = fileManagerAadapter.mSongs;
                currentSongIndex = bundle.getInt("pos");
                setSongData(currentSongIndex);
                NavigationUtils.playingsongServiceFile(context, currentSongIndex);

            } else{
                items = getSongs.songs_data_list;

                switch (type){
                    case "play":
                        isPlaying = true;
                        currentSongIndex = bundle.getInt("pos");
                        setSongData(currentSongIndex);
                        NavigationUtils.playingsongService(context, currentSongIndex);
                        break;
                    case "shuffle_all":
                        isPlaying = true;
                        shuffleAll();
                        setSongData(currentSongIndex);
                        NavigationUtils.playingsongService(context, currentSongIndex);
                        break;
                    case "queue":
                        int currentSongIndex = bundle.getInt("pos");
                        if(!isPlaying){
                            setSongData(currentSongIndex);
                            NavigationUtils.queue_playingsongService(context, currentSongIndex);
                        }else{
                            NavigationUtils.queue_playingsongService(context, currentSongIndex);
                            finish();
                        }
                        break;
                    case "fromAdapter":
                        isPlaying = true;
                        currentSongIndex = bundle.getInt("pos");
                        setSongData(currentSongIndex);
                        //NavigationUtils.playingsongService(context, currentSongIndex);
                        break;
                }
            }

        }

        seekBar_sound.setMax(audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekBar_sound.setProgress(audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC));
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
        previous.setOnClickListener(this);
        next.setOnClickListener(this);
        repeat.setOnClickListener(this);
        shuffle.setOnClickListener(this);
        playPause.setOnClickListener(this);
        timer.setOnClickListener(this);
        countTimer.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bManager.unregisterReceiver(Receiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        bManager.registerReceiver(Receiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        bManager.unregisterReceiver(Receiver);
    }

    private void setSongData(int currentSongIndex) {
        if(mediaPlayer.isPlaying()){
            Song_image = null;
            Song_name = null;
            Artist_name = null;
            Song_duraion = 0;
            Song_data = null;
        }

        playPause.setImageResource(R.drawable.pause_music);
        Song_image = items.get(currentSongIndex).getSong_image();
        Song_name = items.get(currentSongIndex).getSong_name();
        Artist_name = items.get(currentSongIndex).getArtist_name();
        Song_duraion = items.get(currentSongIndex).getSong_duarion();
        Song_data = items.get(currentSongIndex).getSong_data();

        Picasso.with(context)
                .load(Song_image)
                .placeholder(R.drawable.placeholder)
                .into(song_image);

        this.song_name.setText(Song_name + "  " + Artist_name);

        String Duration = utilities.milliSecondsToTimer(Song_duraion);


        this.duration.setText(Duration);
    }

    @Override
    public void onClick(View v) {
        if(v == playPause){
            play_paue();
            NavigationUtils.playPause_playingsongService(context);
        }else if(v == shuffle){
            shuffle();
            NavigationUtils.shuffle_playingsongService(context);
        }else if(v == repeat){
            repeat();
            NavigationUtils.repeat_playingsongService(context);
        }else if(v == next){
            NavigationUtils.next_playingsongService(context);
        }else if(v == previous){
            NavigationUtils.previous_playingsongService(context);
        }else if(v == timer){
            timerDialog();
        }else if(v == countTimer){
            cancel_Timer();
        }
    }

    private void repeat(){
        if(isRepeat){
            isRepeat = false;
            Toast.makeText(context, "Repeat is OFF", Toast.LENGTH_SHORT).show();
            repeat.setImageResource(R.drawable.repeat);
        }else{
            // make repeat to true
            isRepeat = true;
            Toast.makeText(context, "Repeat is ON", Toast.LENGTH_SHORT).show();
            // make shuffle to false
            isShuffle = false;
            repeat.setImageResource(R.drawable.repeat_focus);
            shuffle.setImageResource(R.drawable.shuffle);
        }
    }

    private void shuffle(){
        if(isShuffle){
            isShuffle = false;
            Toast.makeText(context, "Shuffle is OFF", Toast.LENGTH_SHORT).show();
            shuffle.setImageResource(R.drawable.shuffle);
        }else{
            // make repeat to true
            isShuffle= true;
            Toast.makeText(context, "Shuffle is ON", Toast.LENGTH_SHORT).show();
            // make shuffle to false
            isRepeat = false;
            shuffle.setImageResource(R.drawable.shuffle_focus);
            repeat.setImageResource(R.drawable.repeat);
        }
    }

    private void shuffleAll(){
        Random rand = new Random();
        currentSongIndex = getrandomCurrent();
        shuffle();

        NavigationUtils.shuffleAll_playingsongService(context);
    }

    private void check_isShuffle_isRepeat(){
        if(isShuffle){
            isShuffle= true;
            isRepeat = false;
            shuffle.setImageResource(R.drawable.shuffle_focus);
            repeat.setImageResource(R.drawable.repeat);
        }else if(isRepeat){
            isRepeat = true;
            isShuffle = false;
            repeat.setImageResource(R.drawable.repeat_focus);
            shuffle.setImageResource(R.drawable.shuffle);
        }
    }

    private void play_paue(){
        if(isPlaying){
            isPlaying = false;
            playPause.setImageResource(R.drawable.play_music);
        }else{
            isPlaying = true;
            playPause.setImageResource(R.drawable.pause_music);
        }
    }

    private int getrandomCurrent(){
        Random rand = new Random();
        int randowm = rand.nextInt((items.size() - 1) - 0 + 1) + 0;
        return randowm;
    }

    private void timerDialog(){
        MyTimePickerDialog mTimePicker = new MyTimePickerDialog(this, new MyTimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute, int seconds) {
                if(String.valueOf(hourOfDay).equals("0") && String.valueOf(minute).equals("0")
                        && String.valueOf(seconds).equals("0")){
                }else{
                    timer.setVisibility(View.GONE);
                    countTimer.setVisibility(View.VISIBLE);
                    NavigationUtils.timer_playingsongService(context, hourOfDay, minute, seconds);
                }
            }
        },Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND, true);
        mTimePicker.show();
    }

    private void cancel_Timer(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.cancel_timer);
        builder.setMessage(R.string.timer_message);
        builder.setPositiveButton(R.string.cancel_timer, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NavigationUtils.closeTimer_playingsongService(context);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        Intent intent = new Intent(context, playingsongService.class);
        intent.setAction(Constant.SEEKTO_ACTION);
        intent.putExtra("progress", seekBar.getProgress());
        startService(intent);

    }


    public BroadcastReceiver Receiver = new  BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Bundle bundle = intent.getExtras();

            if (action.equals(Constant.SONG_DATA)){

                String Data_type = bundle.getString("type");

                if(Data_type.equals("songData")){
                    currentSongIndex = bundle.getInt("currentSongIndex");
                    setSongData(currentSongIndex);
                }
                if(Data_type.equals("seekbar")){

                    long currentDurations = bundle.getLong("currentDuration");
                    int progress = bundle.getInt("progress");
                    mupdateTimeTask( currentDurations, progress);

                }if(Data_type.equals("seekbar_change")){
                    int currentDurations = bundle.getInt("currentDuration");
                    Log.d("response", String.valueOf(currentDurations));
                }
                if(Data_type.equals("timer")){
                    long seconds = bundle.getLong("seconds");
                    String time = utilities.milliSecondsToTimer(seconds);
                    countTimer.setText(time);
                }
                if(Data_type.equals("timer_finish")){
                    timer.setVisibility(View.VISIBLE);
                    countTimer.setVisibility(View.GONE);
                }
                if(Data_type.equals(Constant.PAUSE_ACTION)){
                    isPlaying = false;
                    playPause.setImageResource(R.drawable.play_music);
                }
            }
        }
    };

    public void mupdateTimeTask(long currentDuration, int progress){
        duration_progress.setText("" + utilities.milliSecondsToTimer(currentDuration));
        seekBar_duration.setProgress(progress);
    }
}
