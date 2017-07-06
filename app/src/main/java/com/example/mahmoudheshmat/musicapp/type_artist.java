package com.example.mahmoudheshmat.musicapp;


import android.os.Parcel;
import android.os.Parcelable;

public class type_artist{

    private String song_image;
    private String song_name;
    private String artist_name;
    private String song_id;
    private String album;
    private long song_duarion;
    private String song_data;
    private String no_songs;
    private int album_id;
    private int artist_id;


    //Artist
    public type_artist(String song_image, String artist_name, int artist_id){
        this.song_image = song_image;
        this.artist_name = artist_name;
        this.artist_id = artist_id;
    }

    public String getSong_image() {
        return song_image;
    }

    public void setSong_image(String song_image) {
        this.song_image = song_image;
    }

    public String getSong_name() {
        return song_name;
    }

    public void setSong_name(String song_name) {
        this.song_name = song_name;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public String getSong_id() {
        return song_id;
    }

    public void setSong_id(String song_id) {
        this.song_id = song_id;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getSong_duarion() {
        return song_duarion;
    }

    public void setSong_duarion(long song_duarion) {
        this.song_duarion = song_duarion;
    }

    public String getSong_data() {
        return song_data;
    }

    public void setSong_data(String song_data) {
        this.song_data = song_data;
    }

    public String getNo_songs() {
        return no_songs;
    }

    public void setNo_songs(String no_songs) {
        this.no_songs = no_songs;
    }

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public int getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(int artist_id) {
        this.artist_id = artist_id;
    }

}
