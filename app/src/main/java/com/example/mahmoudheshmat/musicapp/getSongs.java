package com.example.mahmoudheshmat.musicapp;

import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class getSongs {
    private Context mContext;
    public static ArrayList<songs_item> songs_data_list;
    public static ArrayList<songs_item> album_data_list;
    public static ArrayList<songs_item> Artist_data_list;
    public static ArrayList<songs_item> song_sort_data_list;
    public static ArrayList<songs_item> album_sort_data_list;
    public static ArrayList<songs_item> artist_sort_data_list;
    public static ArrayList<songs_item> artistSongs_data_list;
    public static ArrayList<songs_item> AlbumSongs_data_list;
    public static ArrayList<songs_item> favourites_data_list;
    public static ArrayList<songs_item> playlist_data_list;

    public static List<type_song> search_song;
    public static ArrayList<songs_item> search_song_result;
    public static List<type_album> search_album;
    public static List<type_artist> search_artist;

    private SharedPreferences song_sharedPreferences;
    private SharedPreferences.Editor song_editor;

    private SharedPreferences album_sharedPreferences;
    private SharedPreferences.Editor album_editor;

    public List<type_song> search_songs(Context context, final String newText) {

        mContext = context;
        search_song = new ArrayList<>();
        search_song_result = new ArrayList<>();
        search_song.clear();
        search_song_result.clear();

        new Thread(new Runnable() {

            final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            final String[] cursor_cols = {
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.ALBUM_ID,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.ARTIST_ID};
            final Cursor cursor = mContext.getContentResolver().query(uri,
                    cursor_cols, MediaStore.Audio.Media.TITLE + " LIKE ?", new String[] { "%"+newText+"%" }, null);

            @Override
            public void run() {
                while (cursor.moveToNext()) {
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

                    type_song item = new type_song(String.valueOf(song_Id), image_path, track, artist, album,
                            duration, data, albumId, artistId);

                    songs_item items = new songs_item(String.valueOf(song_Id), image_path, track, artist, album,
                            duration, data, albumId, artistId);

                    search_song.add(item);
                    search_song_result.add(items);

                }
                cursor.close();
            }
        }).start();

        return search_song;

    }

    public List<type_album> search_albums(Context context, final String newText) {

        mContext = context;
        search_album = new ArrayList<>();
        search_album.clear();

        new Thread(new Runnable() {

            final Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
            final String _id = MediaStore.Audio.Albums._ID;
            final String album_name = MediaStore.Audio.Albums.ALBUM;
            final String artist = MediaStore.Audio.Albums.ARTIST;
            final String albumart = MediaStore.Audio.Albums.ALBUM_ART;
            final String tracks = MediaStore.Audio.Albums.NUMBER_OF_SONGS;

            final String[] columns = {_id, album_name, artist, albumart, tracks};

            final Cursor cursor = mContext.getContentResolver().query(uri,
                    columns, MediaStore.Audio.Albums.ALBUM + " LIKE ?", new String[]{"%" + newText + "%"}, null);

            @Override
            public void run() {
                while (cursor.moveToNext()) {
                    String artists = cursor.getString(cursor.getColumnIndex(artist));
                    String album = cursor.getString(cursor.getColumnIndex(album_name));
                    String track = cursor.getString(cursor.getColumnIndex(tracks));
                    int albumId = cursor.getInt(cursor.getColumnIndex(_id));

                    Uri sArtworkUri = Uri
                            .parse("content://media/external/audio/albumart");
                    Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

                    String image_path = albumArtUri.toString();

                    type_album item = new type_album(albumId, image_path, album, artists, track);
                    search_album.add(item);
                }
                cursor.close();
            }
        }).start();
        return search_album;

    }

    public List<type_artist> search_artists(Context context, final String newText) {

        mContext = context;
        search_artist = new ArrayList<>();
        search_artist.clear();

        new Thread(new Runnable() {

            String where = null;

            final Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
            final String _id = MediaStore.Audio.Artists._ID;
            final String artist = MediaStore.Audio.Artists.ARTIST;

            final String[] columns = { _id,artist};

            final Cursor cursor = mContext.getContentResolver().query(uri,
                    columns, MediaStore.Audio.Artists.ARTIST + " LIKE ?", new String[]{"%" + newText + "%"}, null);

            @Override
            public void run() {
                if (cursor.moveToFirst()) {
                    do {
                        String artists = cursor.getString(cursor.getColumnIndex(artist));
                        int artistId = cursor.getInt(cursor.getColumnIndex(_id));


                        Uri sArtworkUri = Uri
                                .parse("content://media/external/audio/albumart");
                        Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, artistId);

                        String image_path = albumArtUri.toString();

                        type_artist item = new type_artist(image_path, artists, artistId);
                        search_artist.add(item);

                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        }).start();
        return search_artist;
    }

    public ArrayList<songs_item> getAllSongs(final Context context, final String sort, final String type) {

        mContext = context;
        songs_data_list = new ArrayList<>();
        songs_data_list.clear();

        final String sortby = getSongType(type);

        new Thread(new Runnable() {

            final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            final String[] cursor_cols = {
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.ALBUM_ID,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.ARTIST_ID};
            final String where = MediaStore.Audio.Media.IS_MUSIC + "=1";
            final Cursor cursor = mContext.getContentResolver().query(uri,
                    cursor_cols, where, null,  sortby+ " "+sort);

            @Override
            public void run() {
                while (cursor.moveToNext()) {

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
                    songs_data_list.add(item);
                }
            }
        }).start();

        return songs_data_list;
    }

    public ArrayList<songs_item> getAllAalbums(Context context, final String sort, final String type){

        mContext = context;
        album_data_list = new ArrayList<>();
        album_data_list.clear();

        final String sortby = getAlbumType(type);

        new Thread(new Runnable() {

            String where = null;

            final Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
            final String _id = MediaStore.Audio.Albums._ID;
            final String album_name = MediaStore.Audio.Albums.ALBUM;
            final String artist = MediaStore.Audio.Albums.ARTIST;
            final String albumart = MediaStore.Audio.Albums.ALBUM_ART;
            final String tracks = MediaStore.Audio.Albums.NUMBER_OF_SONGS;

            final String[] columns = { _id, album_name, artist, albumart, tracks };
            final Cursor cursor = mContext.getContentResolver().query(uri, columns, where,
                    null, sortby + " " + sort);

            @Override
            public void run() {
                while (cursor.moveToNext()){
                    String artists = cursor.getString(cursor.getColumnIndex(artist));
                    String album = cursor.getString(cursor.getColumnIndex(album_name));
                    String track = cursor.getString(cursor.getColumnIndex(tracks));
                    int albumId = cursor.getInt(cursor.getColumnIndex(_id));

                    Uri sArtworkUri = Uri
                            .parse("content://media/external/audio/albumart");
                    Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

                    String image_path = albumArtUri.toString();

                    songs_item item = new songs_item(albumId, image_path, album, artists, track);
                    album_data_list.add(item);
                }
            }
        }).start();

        return album_data_list;
    }

    public ArrayList<songs_item> getAllArtists(Context context, final String sort, final String type) {

        mContext = context;
        Artist_data_list = new ArrayList<>();
        Artist_data_list.clear();

        final String sortby = getArtistType(type);

        new Thread(new Runnable() {

            String where = null;

            final Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
            final String _id = MediaStore.Audio.Artists._ID;

            final String artist = MediaStore.Audio.Artists.ARTIST;

            final String[] columns = { _id,artist};
            Cursor cursor = mContext.getContentResolver().query(uri, columns, where,
                    null, sortby + " " + sort);

            @Override
            public void run() {
                if (cursor.moveToFirst()) {
                    do {
                        String artists = cursor.getString(cursor.getColumnIndex(artist));
                        int artistId = cursor.getInt(cursor.getColumnIndex(_id));

                        Uri sArtworkUri = Uri
                                .parse("content://media/external/audio/albumart");
                        Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, artistId);

                        String image_path = albumArtUri.toString();

                        songs_item item = new songs_item(image_path, artists, artistId);
                        Artist_data_list.add(item);

                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        }).start();
        return Artist_data_list;
    }

    public ArrayList<songs_item> song_sortby(Context context, final String sort, final String type) {

        mContext = context;

        song_sharedPreferences = mContext.getSharedPreferences(Constant.SORT_SONG, Context.MODE_PRIVATE);
        song_sharedPreferences.edit().clear();
        song_editor = song_sharedPreferences.edit();
        song_editor.putString(Constant.SORT_TYPE, sort).commit();
        song_editor.putString(Constant.SORT_BY, type).commit();

        song_sort_data_list = new ArrayList<>();
        song_sort_data_list.clear();

        final String sortby = getSongType(type);

        new Thread(new Runnable() {

            final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            final String[] cursor_cols = {
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.ALBUM_ID,
                    MediaStore.Audio.Media.DURATION ,
                    MediaStore.Audio.Media.ARTIST_ID};
            final String where = MediaStore.Audio.Media.IS_MUSIC + "=1";
            final Cursor cursor = mContext.getContentResolver().query(uri,
                    cursor_cols, where, null, sortby + " " + sort);

            @Override
            public void run() {
                while (cursor.moveToNext()) {
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
                    song_sort_data_list.add(item);

                }
            }
        }).start();

        return song_sort_data_list;
    }

    public ArrayList<songs_item> album_sortby(Context context, final String sort, final String type) {

        mContext = context;

        album_sharedPreferences = mContext.getSharedPreferences(Constant.SORT_ALBUM, Context.MODE_PRIVATE);
        album_sharedPreferences.edit().clear();
        album_editor = album_sharedPreferences.edit();
        album_editor.putString(Constant.SORT_TYPE, sort).commit();
        album_editor.putString(Constant.SORT_BY, type).commit();

        album_sort_data_list = new ArrayList<>();
        album_sort_data_list.clear();

        final String sortby = getAlbumType(type);

        new Thread(new Runnable() {

            String where = null;

            final Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
            final String _id = MediaStore.Audio.Albums._ID;
            final String album_name = MediaStore.Audio.Albums.ALBUM;
            final String artist = MediaStore.Audio.Albums.ARTIST;
            final String albumart = MediaStore.Audio.Albums.ALBUM_ART;
            final String tracks = MediaStore.Audio.Albums.NUMBER_OF_SONGS;

            final String[] columns = { _id, album_name, artist, albumart, tracks };
            final Cursor cursor = mContext.getContentResolver().query(uri, columns, where,
                    null, sortby + " " + sort);

            @Override
            public void run() {
                while (cursor.moveToNext()){
                    String artists = cursor.getString(cursor.getColumnIndex(artist));
                    String album = cursor.getString(cursor.getColumnIndex(album_name));
                    String track = cursor.getString(cursor.getColumnIndex(tracks));
                    int albumId = cursor.getInt(cursor.getColumnIndex(_id));

                    Uri sArtworkUri = Uri
                            .parse("content://media/external/audio/albumart");
                    Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

                    String image_path = albumArtUri.toString();

                    songs_item item = new songs_item(albumId, image_path, album, artists, track);
                    album_sort_data_list.add(item);
                }
            }
        }).start();

        return album_sort_data_list;
    }

    public ArrayList<songs_item> artist_sortby(Context context, final String sort, final String type) {

        mContext = context;

        album_sharedPreferences = mContext.getSharedPreferences(Constant.SORT_ARTIST, Context.MODE_PRIVATE);
        album_sharedPreferences.edit().clear();
        album_editor = album_sharedPreferences.edit();
        album_editor.putString(Constant.SORT_TYPE, sort).commit();
        album_editor.putString(Constant.SORT_BY, type).commit();

        artist_sort_data_list = new ArrayList<>();
        artist_sort_data_list.clear();

        final String sortby = getArtistType(type);

        new Thread(new Runnable() {

            String where = null;

            final Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
            final String _id = MediaStore.Audio.Artists._ID;
            final String artist = MediaStore.Audio.Artists.ARTIST;

            final String[] columns = { _id,artist};
            Cursor cursor = mContext.getContentResolver().query(uri, columns, where,
                    null, sortby + " " + sort);

            @Override
            public void run() {
                if (cursor.moveToFirst()) {
                    do {
                        String artists = cursor.getString(cursor.getColumnIndex(artist));
                        int artistId = cursor.getInt(cursor.getColumnIndex(_id));

                        Uri sArtworkUri = Uri
                                .parse("content://media/external/audio/albumart");
                        Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, artistId);

                        String image_path = albumArtUri.toString();

                        songs_item item = new songs_item(image_path, artists, artistId);
                        artist_sort_data_list.add(item);

                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        }).start();

        return artist_sort_data_list;
    }

    public ArrayList<songs_item> getAllArtistSongs(Context context ,final int artistId) {

        mContext = context;

        artistSongs_data_list = new ArrayList<>();
        artistSongs_data_list.clear();

        new Thread(new Runnable() {

            final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            final String[] cursor_cols = {
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.ALBUM_ID,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.ARTIST_ID};
            final Cursor cursor = mContext.getContentResolver().query(uri,
                    cursor_cols, MediaStore.Audio.Media.ARTIST_ID + " = ?", new String[] {String.valueOf(artistId)}, null);

            @Override
            public void run() {
                while (cursor.moveToNext()) {
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
                    artistSongs_data_list.add(item);

                }
            }
        }).start();

        return  artistSongs_data_list;

    }

    public ArrayList<songs_item> getAllAlbumSsongs(Context context ,final int albumId) {

        mContext = context;

        AlbumSongs_data_list = new ArrayList<>();
        AlbumSongs_data_list.clear();

        new Thread(new Runnable() {

            final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            final String[] cursor_cols = {
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.ALBUM_ID,
                    MediaStore.Audio.Media.DURATION ,
                    MediaStore.Audio.Media.ARTIST_ID};
            final String where = MediaStore.Audio.Media.TITLE + "=A";
            final Cursor cursor = mContext.getContentResolver().query(uri,
                    cursor_cols, MediaStore.Audio.Media.ALBUM_ID + " = ?", new String[] {String.valueOf(albumId)}, null);

            @Override
            public void run() {
                while (cursor.moveToNext()) {
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
                    AlbumSongs_data_list.add(item);

                }
            }
        }).start();

        return AlbumSongs_data_list;
    }

    public ArrayList<songs_item> Favourites(Context context , final ArrayList songsId) {

        mContext = context;

        favourites_data_list = new ArrayList<>();
        favourites_data_list.clear();

        final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        final String[] cursor_cols = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION ,
                MediaStore.Audio.Media.ARTIST_ID};

        final StringBuilder selection = new StringBuilder();
        selection.append(BaseColumns._ID + " IN (");
        for (int i = 0; i < songsId.size(); i++) {
            selection.append(songsId.get(i));
            if (i < songsId.size() - 1) {
                selection.append(",");
            }
        }
        selection.append(")");


        final Cursor cursor = mContext.getContentResolver().query(uri,
                cursor_cols, selection.toString(), null, null);

        while (cursor.moveToNext()) {
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
            favourites_data_list.add(item);

        }


        return favourites_data_list;
    }

    public ArrayList<songs_item> Playlist(Context context , final ArrayList<playlist_items> songsId) {

        mContext = context;

        playlist_data_list = new ArrayList<>();
        playlist_data_list.clear();

        final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        final String[] cursor_cols = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION ,
                MediaStore.Audio.Media.ARTIST_ID};

        for (int i = 0; i<songsId.size() ; i++){
            final Cursor cursor = mContext.getContentResolver().query(uri,
                    cursor_cols, MediaStore.Audio.Media._ID + " = ?", new String[]{String.valueOf(songsId.get(i).getSong_id())}, null);

            while (cursor.moveToNext()) {
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
                playlist_data_list.add(item);

            }
        }


        return playlist_data_list;
    }

    private String getSongType(String type){
        if(type.equals(Constant.MEDIASTORE_TITLE))
            return MediaStore.Audio.Media.TITLE;
        else if(type.equals(Constant.MEDIASTORE_DATE))
            return MediaStore.Audio.Media.DATE_ADDED;
        else if(type.equals(Constant.MEDIASTORE_ALBUM))
            return MediaStore.Audio.Media.ALBUM;
        else if(type.equals(Constant.MEDIASTORE_ARTIST))
            return MediaStore.Audio.Media.ARTIST;
        else
            return MediaStore.Audio.Media.DURATION;
    }

    private String getAlbumType(String type){
        if(type.equals(Constant.MEDIASTORE_TITLE_ALBUM))
            return MediaStore.Audio.Albums.ALBUM;
        else
            return MediaStore.Audio.Albums.NUMBER_OF_SONGS;
    }

    private String getArtistType(String type){
        if(type.equals(Constant.MEDIASTORE_TITLE_ARTIST))
            return MediaStore.Audio.Artists.ARTIST;
        else if(type.equals(Constant.MEDIASTORE_NUMBER_OF_TRACKS_ARTIST))
            return MediaStore.Audio.Artists.NUMBER_OF_TRACKS;
        else
            return MediaStore.Audio.Artists.NUMBER_OF_ALBUMS;
    }
}

