package com.example.mahmoudheshmat.musicapp;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class songs_item extends ArrayList<songs_item> implements Parcelable {

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

    // song
    public songs_item(String song_id, String song_image, String song_name, String artist_name,
                      String album, long song_duarion, String song_data,int album_id, int artist_id){
        this.song_id = song_id;
        this.song_image = song_image;
        this.song_name = song_name;
        this.artist_name = artist_name;
        this.album = album;
        this.song_duarion = song_duarion;
        this.song_data = song_data;
        this.album_id = album_id;
        this.artist_id = artist_id;
    }

    //Album
    public songs_item(int album_id ,String song_image, String song_name, String artist_name, String no_songs){
        this.song_image = song_image;
        this.song_name = song_name;
        this.artist_name = artist_name;
        this.no_songs = no_songs;
        this.album_id = album_id;
    }

    //Artist
    public songs_item(String song_image, String artist_name, int artist_id){
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

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int arg1) {
        // TODO Auto-generated method stub
        dest.writeString(song_id);
        dest.writeString(song_image);
        dest.writeString(song_name);
        dest.writeString(artist_name);
        dest.writeString(album);
        dest.writeString(song_data);
        dest.writeLong(song_duarion);
    }

    public songs_item(Parcel in) {
        this.song_id = in.readString();
        this.song_image = in.readString();
        this.song_name = in.readString();
        this.artist_name = in.readString();
        this.album = in.readString();
        this.song_duarion = in.readLong();
        this.song_data = in.readString();
    }

    public static final Parcelable.Creator<songs_item> CREATOR = new Parcelable.Creator<songs_item>() {
        public songs_item createFromParcel(Parcel in) {
            return new songs_item(in);
        }

        public songs_item[] newArray(int size) {
            return new songs_item[size];
        }
    };
}
