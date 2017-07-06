package com.example.mahmoudheshmat.musicapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

public class NavigationUtils {

    public static void navigateToSearch(Activity context) {
        final Intent intent = new Intent(context, searchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.setAction(Constant.NAVIGATE_SEARCH);
        context.startActivity(intent);
    }

    public static void album_playingsongActivity(Context context, int position) {
        Intent intent = new Intent(context, playingsongActivity.class);
        intent.putExtra("type", "album");
        intent.putExtra("pos", position);
        context.startActivity(intent);
    }

    public static void artist_playingsongActivity(Context context, int position) {
        Intent intent = new Intent(context, playingsongActivity.class);
        intent.putExtra("type", "artist");
        intent.putExtra("pos", position);
        context.startActivity(intent);
    }

    public static void artist_playingsongService(Context context, int currentSongIndex) {
        Intent intent = new Intent(context, playingsongService.class);
        intent.setAction(Constant.ARTIST);
        intent.putExtra("currentSongIndex", currentSongIndex);
        context.startService(intent);
    }

    public static void album_playingsongService(Context context, int currentSongIndex) {
        Intent intent = new Intent(context, playingsongService.class);
        intent.setAction(Constant.ALBUM);
        intent.putExtra("currentSongIndex", currentSongIndex);
        context.startService(intent);
    }

    public static void favourite_playingsongService(Context context, int currentSongIndex) {
        Intent intent = new Intent(context, playingsongService.class);
        intent.setAction(Constant.FAVOURITES);
        intent.putExtra("currentSongIndex", currentSongIndex);
        context.startService(intent);
    }

    public static void playlist_playingsongService(Context context, int currentSongIndex) {
        Intent intent = new Intent(context, playingsongService.class);
        intent.setAction(Constant.PLAYLIST);
        intent.putExtra("currentSongIndex", currentSongIndex);
        context.startService(intent);
    }

    public static void shuffleAll_playingsongService(Context context) {
        Intent intent = new Intent(context, playingsongService.class);
        intent.setAction(Constant.SHUFFLE_ACTION);
        context.startService(intent);
    }

    public static void queue_playingsongService(Context context, int currentSongIndex) {
        Intent intent = new Intent(context, playingsongService.class);
        intent.setAction(Constant.QUEUE_ACTION);
        intent.putExtra("currentSongIndex", currentSongIndex);
        context.startService(intent);
    }

    public static void playingsongService(Context context, int currentSongIndex) {
        Intent intent = new Intent(context, playingsongService.class);
        intent.putExtra("currentSongIndex", currentSongIndex);
        intent.setAction(Constant.PLAY_ACTION);
        context.startService(intent);
    }

    public static void playingsongServiceFromAdapter(Context context, int currentSongIndex) {
        Intent intent = new Intent(context, playingsongService.class);
        intent.putExtra("currentSongIndex", currentSongIndex);
        intent.setAction(Constant.PLAY_ACTION_ADAPTER);
        context.startService(intent);
    }

    public static void playingsongServiceSearch(Context context, int currentSongIndex) {
        Intent intent = new Intent(context, playingsongService.class);
        intent.putExtra("currentSongIndex", currentSongIndex);
        intent.setAction(Constant.PLAY_SEARCH);
        context.startService(intent);
    }

    public static void playingsongServiceFile(Context context, int currentSongIndex) {
        Intent intent = new Intent(context, playingsongService.class);
        intent.putExtra("currentSongIndex", currentSongIndex);
        intent.setAction(Constant.FILES);
        context.startService(intent);
    }

    public static void playPause_playingsongService(Context context) {
        Intent intent = new Intent(context, playingsongService.class);
        intent.setAction(Constant.PLAYPAUSE_ACTION);
        context.startService(intent);
    }

    public static void head_playingsongService(Context context) {
        Intent intent = new Intent(context, playingsongService.class);
        intent.setAction(Constant.HEADSET);
        context.startService(intent);
    }

    public static void shuffle_playingsongService(Context context) {
        Intent intent = new Intent(context, playingsongService.class);
        intent.setAction(Constant.SHUFFLE_ACTION);
        context.startService(intent);
    }

    public static void repeat_playingsongService(Context context) {
        Intent intent = new Intent(context, playingsongService.class);
        intent.setAction(Constant.REPEAT_ACTION);
        context.startService(intent);
    }

    public static void next_playingsongService(Context context) {
        Intent intent = new Intent(context, playingsongService.class);
        intent.setAction(Constant.NEXT_ACTION);
        context.startService(intent);
    }

    public static void previous_playingsongService(Context context) {
        Intent intent = new Intent(context, playingsongService.class);
        intent.setAction(Constant.PREVIOUS_ACTION);
        context.startService(intent);
    }

    public static void timer_playingsongService(Context context, int hour, int minute, int second) {
        Intent intent = new Intent(context, playingsongService.class);
        intent.setAction(Constant.TIMER_ACTION);
        intent.putExtra("hour", hour);
        intent.putExtra("minute", minute);
        intent.putExtra("second", second);
        context.startService(intent);
    }

    public static void bottom_playingsongService(Context context, int currentSongIndex) {
        Intent intent = new Intent(context, playingsongService.class);
        intent.setAction(Constant.BOTTOM);
        intent.putExtra("currentSongIndex", currentSongIndex);
        context.startService(intent);
    }

    public static void closeTimer_playingsongService(Context context) {
        Intent intent = new Intent(context, playingsongService.class);
        intent.setAction(Constant.CLOSE_TIMER_ACTION);
        context.startService(intent);
    }


    public static void play_playingsongActivity(Context context, int currentSongIndex) {
        Intent playsong = new Intent(context, playingsongActivity.class);
        playsong.putExtra("type", "play");
        playsong.putExtra("pos", currentSongIndex);
        context.startActivity(playsong);
    }

    public static void play_playingsongActivityFromAdapter(Context context, int currentSongIndex) {
        Intent playsong = new Intent(context, playingsongActivity.class);
        playsong.putExtra("type", "fromAdapter");
        playsong.putExtra("pos", currentSongIndex);
        context.startActivity(playsong);
    }

    public static void play_songActivity_search(Context context, int currentSongIndex) {
        Intent playsong = new Intent(context, playingsongActivity.class);
        playsong.putExtra("type", "play_search");
        playsong.putExtra("pos", currentSongIndex);
        context.startActivity(playsong);
    }


    public static void favourite_playingsongActivity(Context context, int currentSongIndex) {
        Intent playsong = new Intent(context, playingsongActivity.class);
        playsong.putExtra("type", "favourite");
        playsong.putExtra("pos", currentSongIndex);
        context.startActivity(playsong);
    }

    public static void queue_playingsongActivity(Context context, int currentSongIndex) {
        Intent queue = new Intent(context, playingsongActivity.class);
        queue.putExtra("type", "queue");
        queue.putExtra("pos", currentSongIndex);
        context.startActivity(queue);
    }

    public static void card_playingsongActivity(Context context, int currentSongIndex) {
        Intent intent = new Intent(context, playingsongActivity.class);
        intent.putExtra("type", "play");
        intent.putExtra("pos", currentSongIndex);
        context.startActivity(intent);
    }

    public static void file_playingsongActivity(Context context, int currentSongIndex) {
        Intent intent = new Intent(context, playingsongActivity.class);
        intent.putExtra("type", "fromFile");
        intent.putExtra("pos", currentSongIndex);
        context.startActivity(intent);
    }

    public static void card_playlistActivity(Context context, int currentSongIndex) {
        Intent intent = new Intent(context, playingsongActivity.class);
        intent.putExtra("type", "playlist");
        intent.putExtra("pos", currentSongIndex);
        context.startActivity(intent);
    }

    public static void navigateSongs(Context context) {
        context.startActivity(new Intent(context, songs.class));
    }

    public static void navigatoNotes(Context context) {
        context.startActivity(new Intent(context, notesActivity.class));
    }

    public static void navigatoPlaylists(Context context) {
        context.startActivity(new Intent(context, playlistActivity.class));
    }

    public static void navigatoPlaylists(Activity context) {
        final Intent intent = new Intent(context, playlistActivity.class);
        context.startActivity(intent);
    }

    public static void navigatoYoutube(Context context) {
        context.startActivity(new Intent(context, youtubeActivity.class));
    }

}
