package com.example.mahmoudheshmat.musicapp;


import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class FileLoader {

    public static ArrayList<type_file> getAllDirectoriesFromSDCard() {
        ArrayList<type_file> list = new ArrayList<>();
        File file[] = Environment.getExternalStorageDirectory().listFiles();

        for (File f : file)
        {
            if (f.isDirectory()) {
                type_file t_file = new type_file(f.getName(), f.getAbsolutePath());
                list.add(t_file);
            }else if(f.isFile()){
                if(f.getName().endsWith(".MP3") || f.getName().endsWith(".mp3")){
                    type_file t_file = new type_file(f.getName(), f.getAbsolutePath());
                    list.add(t_file);
                }
            }
        }
        return list;
    }

    public static ArrayList<type_file> getFilesFromPath(File f) {
        ArrayList<type_file> list = new ArrayList<>();
        File[] files = f.listFiles();
        for (File inFile : files) {
            if (inFile.isDirectory()) {
                type_file t_file = new type_file(inFile.getName(), inFile.getAbsolutePath());
                list.add(t_file);

            }else{

                type_file t_file = new type_file(inFile.getName(), inFile.getAbsolutePath());
                list.add(t_file);
            }
        }
        return list;
    }

    public static songs_item getSongFromPath(final Context context, String songPath) {

        final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        final String[] cursor_cols = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ARTIST_ID};
        final Cursor cursor = context.getContentResolver().query(uri,
                cursor_cols, MediaStore.Audio.Media.DATA +" = ?", new String[]{songPath},  sortOrder);


        if (cursor.moveToNext()) {

            String artist = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            String album = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            String track = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            String data = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            int albumId = cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
            Long song_Id = cursor.getLong(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
            int duration = cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
            int artistId = cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID));

            Uri sArtworkUri = Uri
                    .parse("content://media/external/audio/albumart");
            Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

            String image_path = albumArtUri.toString();

            songs_item item = new songs_item(String.valueOf(song_Id), image_path, track, artist, album,
                    duration, data, albumId, artistId);
            return item;
        }

        return null;
    }

}
