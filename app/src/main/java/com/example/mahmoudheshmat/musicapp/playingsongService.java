package com.example.mahmoudheshmat.musicapp;


import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.audiofx.AudioEffect;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class playingsongService extends Service implements MediaPlayer.OnCompletionListener{

    MediaPlayer mediaPlayer;

    private boolean isShuffle = false;
    private boolean isRepeat = false;
    private static boolean isNext = false;
    private static boolean isPrevious = false;
    private static boolean isArtist = false;
    private static boolean isAlbum = false;
    private static boolean isFavourite = false;
    private static boolean isPlaylist = false;
    private static boolean isAdapter = false;
    String Song_data;

    ArrayList<songs_item> items;
    ArrayList<songs_item> queue_items;
    ArrayList<Integer> queue_currentSong;

    private static int currentSongIndex = 0;

    private static int queueSong_Position = 0;
    private static int queuenowPlaying_Position = 0;
    private static int queuecurrentSongIndex = 0;

    CountDownTimer countDownTimer;

    Utilities utilities;
    private final Handler handler = new Handler();

    NotificationManager notificationManager;
    android.support.v4.app.NotificationCompat.Builder mBuilder;
    RemoteViews widget_customBig;
    RemoteViews widget_customStandard;
    static int id =1;

    private final IBinder mBinder = new localBinder();

    Bundle bundle = null;

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = new MediaPlayer();
        //mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setOnCompletionListener(this);
        items = getSongs.songs_data_list;
        queue_items = new ArrayList<>();
        queue_currentSong = new ArrayList<>();
        utilities = new Utilities();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bundle = intent.getExtras();
        onStartCommandSwitch(intent);
        return START_NOT_STICKY;
    }

    //get all action from activities while service run
    public void onStartCommandSwitch(Intent intent) {

        switch (intent.getAction()) {

            case Constant.PLAY_ACTION:
                if (isAlbum || isArtist) {
                    items = getSongs.songs_data_list;
                    isAlbum = false;
                    isArtist = false;
                }
                currentSongIndex = bundle.getInt("currentSongIndex");
                play(currentSongIndex);
                break;
            case Constant.PLAY_ACTION_ADAPTER:
                if (isAlbum || isArtist) {
                    items = getSongs.songs_data_list;
                    isAlbum = false;
                    isArtist = false;
                }
                isAdapter = true;
                currentSongIndex = bundle.getInt("currentSongIndex");
                play(currentSongIndex);
                break;
            case Constant.PLAYPAUSE_ACTION:
                play_pause();
                break;
            case Constant.NEXT_ACTION:
                next();
                break;
            case Constant.PREVIOUS_ACTION:
                previous();
                break;
            case Constant.SHUFFLE_ACTION:
                shuffle();
                break;
            case Constant.REPEAT_ACTION:
                repeat();
                break;
            case Constant.TIMER_ACTION:
                int hour = bundle.getInt("hour");
                int minute = bundle.getInt("minute");
                int second = bundle.getInt("second");

                long number_of_second = (hour * 60 * 60 * 1000) + (minute * 60 * 1000) + (second * 1000);
                timer(number_of_second);
                break;
            case Constant.CLOSE_TIMER_ACTION:
                countDownTimer.cancel();
                sendBroadCast_Timer();
                break;
            case Constant.SEEKTO_ACTION:
                int progress = bundle.getInt("progress");
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = utilities.progressToTimer(progress, totalDuration);
                mediaPlayer.seekTo(currentPosition);
                break;
            case Constant.ARTIST:
                if (!isArtist) {
                    items = getSongs.artistSongs_data_list;
                }
                isArtist = true;
                isAlbum = false;
                currentSongIndex = bundle.getInt("currentSongIndex");
                play(currentSongIndex);
                break;
            case Constant.ALBUM:
                if (!isAlbum) {
                    items = getSongs.AlbumSongs_data_list;
                }
                isAlbum = true;
                isArtist = false;
                currentSongIndex = bundle.getInt("currentSongIndex");
                sendBroadCast_Bottom_Album(currentSongIndex);
                play(currentSongIndex);
                break;
            case Constant.FAVOURITES:
                if (!isFavourite) {
                    items = getSongs.favourites_data_list;
                }
                isAlbum = true;
                isArtist = false;
                Bundle bundle = intent.getExtras();
                currentSongIndex = bundle.getInt("currentSongIndex");
                play(currentSongIndex);
                break;
            case Constant.FILES:
                items = fileManagerAadapter.mSongs;
                bundle = intent.getExtras();
                currentSongIndex = bundle.getInt("currentSongIndex");
                play(currentSongIndex);
                break;
            case Constant.PLAYLIST:
                if (!isPlaylist) {
                    items = getSongs.playlist_data_list;
                }
                isAlbum = true;
                isArtist = false;
                bundle = intent.getExtras();
                currentSongIndex = bundle.getInt("currentSongIndex");
                play(currentSongIndex);
                break;
            case Constant.QUEUE_ACTION:
                queue_items.add(items.get(queueSong_Position));
                queue_currentSong.add(queueSong_Position);
                if (mediaPlayer.isPlaying()) {

                } else {
                    onComplete();
                }
                break;
            case Constant.PLAY_SEARCH:
                items = getSongs.search_song_result;
                play(currentSongIndex);
                break;
            case Constant.HEADSET:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    sendBroadCastToadapter_PlayPause(Constant.PLAY_ACTION);
                    sendBroadCastToActivity_PlayPause(Constant.PAUSE_ACTION);
                    updateNotification(Constant.PLAY_ACTION);
                }
                break;
            case Constant.BOTTOM:
                bundle = intent.getExtras();
                isAlbum = false;
                isArtist = false;
                items = getSongs.songs_data_list;
                currentSongIndex = bundle.getInt("currentSongIndex");
                sendBroadCast_Bottom(currentSongIndex);
                play(currentSongIndex);
                break;

        }
        setUpHandler();
    }

    // Handler send seekBar progress every second
    private void setUpHandler() {
        handler.removeCallbacks(sendUpdatestoUI);
        handler.postDelayed(sendUpdatestoUI, 100);
    }

    private Runnable sendUpdatestoUI = new Runnable() {
        @Override
        public void run() {
            LogMediaPostion();
            handler.postDelayed(this, 1000);
        }
    };

    private void LogMediaPostion() {
        if(mediaPlayer.isPlaying()){
            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();
            int progress = (int)(utilities.getProgressPercentage(currentDuration, totalDuration));

            Intent intent = new Intent();
            intent.setAction(Constant.SONG_DATA);
            intent.putExtra("type", "seekbar");
            intent.putExtra("currentDuration", currentDuration);
            intent.putExtra("progress", progress);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }

    }

    //play music media player
    private void play(int CurrentSongIndex){
        String songName = items.get(CurrentSongIndex).getSong_name();
        String artistName = items.get(CurrentSongIndex).getArtist_name();
        int albumId = items.get(CurrentSongIndex).getAlbum_id();
        Bitmap songImage = getBitmapImage(albumId);

        //send playing music as notification
        sendNotification(songName , artistName, songImage);

        Song_data = items.get(CurrentSongIndex).getSong_data();

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(Song_data);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //method for playing queue songs
    private void play_queue(int CurrentSongIndex){

        Song_data = queue_items.get(CurrentSongIndex).getSong_data();

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(Song_data);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //method for repeat song
    private void repeat(){

        if(isRepeat){
            isRepeat = false;
        }else{
            // make repeat to true
            isRepeat = true;
        }
    }

    //method for shuffle songs
    private void shuffle(){
        if(isShuffle){
            isShuffle = false;
        }else{
            // make repeat to true
            isShuffle= true;
            // make shuffle to false
            isRepeat = false;
        }

    }

    //method for previous songs
    private void previous(){

        if (isShuffle) {
            // shuffle is on - play a random song
            Random rand = new Random();
            currentSongIndex = rand.nextInt((items.size() - 1) - 0 + 1) + 0;

            if(currentSongIndex > 0){
                play(currentSongIndex - 1);
                currentSongIndex = currentSongIndex - 1;
            }else{
                // play last song
                play(items.size() - 1);
                currentSongIndex = items.size() - 1;
            }
            sendBroadCast(currentSongIndex);

        }else if(queue_items.size() >0){
            play_queue(queuenowPlaying_Position);
            queuecurrentSongIndex = queue_currentSong.get(queuenowPlaying_Position);
            queue_items.remove(queuenowPlaying_Position);
            queue_currentSong.remove(queuenowPlaying_Position);
            sendBroadCast(queuecurrentSongIndex);

        } else{
            if(currentSongIndex > 0){
                play(currentSongIndex - 1);
                currentSongIndex = currentSongIndex - 1;
            }else{
                // play last song
                play(items.size() - 1);
                currentSongIndex = items.size() - 1;
            }
            sendBroadCast(currentSongIndex);
        }

        sendBroadCast_Bottom(currentSongIndex);
    }

    //method for next songs
    private void next(){

        if (isShuffle) {
            // shuffle is on - play a random song
            Random rand = new Random();
            currentSongIndex = rand.nextInt((items.size() - 1) - 0 + 1) + 0;

            if(currentSongIndex < (items.size() - 1)){
                play(currentSongIndex + 1);
                currentSongIndex = currentSongIndex + 1;
            }else{
                // play first song
                play(0);
                currentSongIndex = 0;
            }
            sendBroadCast(currentSongIndex);

        }else if(queue_items.size() >0){
            play_queue(queuenowPlaying_Position);
            queuecurrentSongIndex = queue_currentSong.get(queuenowPlaying_Position);
            queue_items.remove(queuenowPlaying_Position);
            queue_currentSong.remove(queuenowPlaying_Position);
            sendBroadCast(queuecurrentSongIndex);

        }else{
            if(currentSongIndex < (items.size() - 1)){
                play(currentSongIndex + 1);
                currentSongIndex = currentSongIndex + 1;
            }else{
                // play first song
                play(0);
                currentSongIndex = 0;
            }
            sendBroadCast(currentSongIndex);
        }


        if(isAlbum) {
            sendBroadCast_Bottom_Album(currentSongIndex);
        }else{
            sendBroadCast_Bottom(currentSongIndex);
        }
    }

    //method for making timer for close song
    private void timer(long seconds){
        countDownTimer = new CountDownTimer(seconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                sendBroadCast_Timer(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                mediaPlayer.stop();
                sendBroadCast_Timer();
            }
        }.start();
    }

    //method for play or pause the song
    private void play_pause(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            sendBroadCastToadapter_PlayPause(Constant.PLAY_ACTION);
            updateNotification(Constant.PLAY_ACTION);

        }else{
            mediaPlayer.start();
            sendBroadCastToadapter_PlayPause(Constant.PAUSE_ACTION);
            updateNotification(Constant.PAUSE_ACTION);
        }
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) mediaPlayer.release();
        super.onDestroy();
    }

    //Make binder
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class localBinder extends Binder {
        public playingsongService getService(){
            return playingsongService.this;
        }
    }

    //get mediaPlayer sessionId for equalizer
    public int getAudioSessionId(){
        return mediaPlayer.getAudioSessionId();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    //On complete song listener
    @Override
    public void onCompletion(MediaPlayer mp) {
        if(!isNext || !isPrevious){
            onComplete();
        }
        isNext = false;
        isPrevious = false;
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
            sendBroadCast(currentSongIndex);

        }else if(queue_items.size() > 0){
            play_queue(queuenowPlaying_Position);
            queuecurrentSongIndex = queue_currentSong.get(queuenowPlaying_Position);
            queue_items.remove(queuenowPlaying_Position);
            queue_currentSong.remove(queuenowPlaying_Position);
            sendBroadCast(queuecurrentSongIndex);

        }else{
            // no repeat or shuffle ON - play next song
            if (currentSongIndex < (items.size() - 1)) {
                play(currentSongIndex + 1);
                currentSongIndex = currentSongIndex + 1;
            } else {
                // play first song
                play(0);
                currentSongIndex = 0;
            }

            sendBroadCast_Bottom(currentSongIndex);
            sendBroadCast(currentSongIndex);
        }
    }

    //send broadcast to playing song activity
    private void sendBroadCast(int currentSongIndex){
        Intent intent = new Intent();
        intent.setAction(Constant.SONG_DATA);
        intent.putExtra("type", "songData");
        intent.putExtra("currentSongIndex", currentSongIndex);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    //send broadcast to playing song activity for play or pause the song
    private void sendBroadCastToActivity_PlayPause(String type){
        Intent intent = new Intent();
        intent.setAction(Constant.SONG_DATA);
        intent.putExtra("type", type);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    //send broadcast to bottom playing song for play or pause the song
    private void sendBroadCastToadapter_PlayPause(String type){
        Intent intent = new Intent();
        intent.setAction(Constant.BOTTOM);
        intent.putExtra("type", type);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    //send broadcast to playing song activity for make timer
    private void sendBroadCast_Timer( long millisUntilFinished){
        Intent intent = new Intent();
        intent.setAction(Constant.SONG_DATA);
        intent.putExtra("type", "timer");
        intent.putExtra("seconds", millisUntilFinished);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    //send broadcast to playing song activity for close the timer
    private void sendBroadCast_Timer(){
        Intent intent = new Intent();
        intent.setAction(Constant.SONG_DATA);
        intent.putExtra("type", "timer_finish");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    //send broadcast to bottom playing song for displaying song data
    private void sendBroadCast_Bottom(int currentSongIndex){
        Intent intent = new Intent();
        intent.setAction(Constant.BOTTOM);
        intent.putExtra("currentSongIndex", currentSongIndex);
        intent.putExtra("type", "songData");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    //send broadcast to bottom playing song for displaying album data
    private void sendBroadCast_Bottom_Album(int currentSongIndex){
        Intent intent = new Intent();
        intent.setAction(Constant.BOTTOM);
        intent.putExtra("currentSongIndex", currentSongIndex);
        intent.putExtra("type", "album");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    //send notification
    private void sendNotification(String songName , String artistName, Bitmap songImage) {

        notificationManager =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        widget_customBig = new RemoteViews(getPackageName(), R.layout.widget_custom_big);
        if(songImage == null){
            widget_customBig.setImageViewResource(R.id.imageViewAlbumArt, R.drawable.placeholder);
        }else{
            widget_customBig.setImageViewBitmap(R.id.imageViewAlbumArt, songImage);
        }
        widget_customBig.setTextViewText(R.id.textSongName, songName);
        widget_customBig.setTextViewText(R.id.textAlbumName, artistName);

        widget_customStandard = new RemoteViews(getPackageName(), R.layout.widget_custom_standard);
        if(songImage == null){
            widget_customStandard.setImageViewResource(R.id.imageViewAlbumArt, R.drawable.placeholder);
        }else{
            widget_customStandard.setImageViewBitmap(R.id.imageViewAlbumArt, songImage);
        }
        widget_customStandard.setTextViewText(R.id.textSongName, songName);
        widget_customStandard.setTextViewText(R.id.textAlbumName, artistName);

        mBuilder = new NotificationCompat.Builder(this)
                .setAutoCancel(false)
                .setSmallIcon(R.drawable.notify_icon)
                .setCustomContentView(widget_customStandard)
                .setCustomBigContentView(widget_customBig);
                //.setOngoing(true);

        Notification notification = mBuilder.build();

        setListeners(widget_customStandard, widget_customBig, this);

        notificationManager.notify(id, notification);
    }

    private void updateNotification(String type) {

        if(type.equals(Constant.PLAY_ACTION) && !mediaPlayer.isPlaying()){
            widget_customBig.setViewVisibility(R.id.btnPause, View.GONE);
            widget_customBig.setViewVisibility(R.id.btnPlay, View.VISIBLE);

        }else if(type.equals(Constant.PAUSE_ACTION)  && mediaPlayer.isPlaying()){
            widget_customBig.setViewVisibility(R.id.btnPause, View.VISIBLE);
            widget_customBig.setViewVisibility(R.id.btnPlay, View.GONE);
        }

        Notification notification = mBuilder.build();
        notificationManager.notify(id, notification);

    }

    private void setListeners(RemoteViews widgetCustomStandard, RemoteViews widget_customBig, Context context) {

        Intent previous = new Intent(Constant.NOTIFY_PREVIOUS);
        Intent pause = new Intent(Constant.NOTIFY_PAUSE);
        Intent next = new Intent(Constant.NOTIFY_NEXT);
        Intent play = new Intent(Constant.NOTIFY_PLAY);
        Intent cancel = new Intent(Constant.NOTIFY_CANCEL);

        PendingIntent pPrevious = PendingIntent.getBroadcast(context, 0, previous, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pPause = PendingIntent.getBroadcast(context, 0, pause, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pNext = PendingIntent.getBroadcast(context, 0, next, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pPlay = PendingIntent.getBroadcast(context, 0, play, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pCancel = PendingIntent.getBroadcast(context, 0, cancel, PendingIntent.FLAG_UPDATE_CURRENT);

        widgetCustomStandard.setOnClickPendingIntent(R.id.btnPrevious, pPrevious);
        widgetCustomStandard.setOnClickPendingIntent(R.id.btnPause, pPause);
        widgetCustomStandard.setOnClickPendingIntent(R.id.btnNext, pNext);
        widgetCustomStandard.setOnClickPendingIntent(R.id.btnPlay, pPlay);
        widgetCustomStandard.setOnClickPendingIntent(R.id.btnDelete, pCancel);

        widget_customBig.setOnClickPendingIntent(R.id.btnPrevious, pPrevious);
        widget_customBig.setOnClickPendingIntent(R.id.btnPause, pPause);
        widget_customBig.setOnClickPendingIntent(R.id.btnNext, pNext);
        widget_customBig.setOnClickPendingIntent(R.id.btnPlay, pPlay);
        widget_customBig.setOnClickPendingIntent(R.id.btnDelete, pCancel);
    }

    //Convert the album id to bitmap image
    private Bitmap getBitmapImage(int albumId){
        Uri sArtworkUri = Uri
                .parse("content://media/external/audio/albumart");
        Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), albumArtUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }


}
