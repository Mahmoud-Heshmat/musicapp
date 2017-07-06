package com.example.mahmoudheshmat.musicapp;


public class playlist_items {

    private int playlist_id;
    private String playlist_name;
    private int  playlist_songID;
    private int song_id;

    public playlist_items(int playlist_id, String playlist_name){
        this.playlist_id = playlist_id;
        this.playlist_name = playlist_name;
    }

    public playlist_items(int playlist_songID, int playlist_id, int song_id){
        this.playlist_songID = playlist_songID;
        this.playlist_id = playlist_id;
        this.song_id = song_id;
    }


    public int getPlaylist_id() {
        return playlist_id;
    }

    public void setPlaylist_id(int playlist_id) {
        this.playlist_id = playlist_id;
    }

    public String getPlaylist_name() {
        return playlist_name;
    }

    public void setPlaylist_name(String playlist_name) {
        this.playlist_name = playlist_name;
    }

    public int getPlaylist_songID() {
        return playlist_songID;
    }

    public void setPlaylist_songID(int playlist_songID) {
        this.playlist_songID = playlist_songID;
    }

    public int getSong_id() {
        return song_id;
    }

    public void setSong_id(int song_id) {
        this.song_id = song_id;
    }
}
