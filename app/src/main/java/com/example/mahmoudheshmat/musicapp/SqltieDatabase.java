package com.example.mahmoudheshmat.musicapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class SqltieDatabase extends SQLiteOpenHelper {

    public static final String DBName = "music.db";
    public static final int version = 3;

    SqltieDatabase(Context context) {
        super(context, DBName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table IF NOT EXISTS favourite (song_id INTEGER)");
        db.execSQL("create table IF NOT EXISTS music_notes (id INTEGER primary key, title text, noteDate text, desc text)");
        db.execSQL("create table IF NOT EXISTS playlist (id INTEGER primary key, name text)");
        db.execSQL("create table IF NOT EXISTS playlist_songs (id INTEGER primary key, playlist_id INTEGER, song_id INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP table if EXISTS favourite");
        db.execSQL("DROP table if EXISTS music_notes");
        db.execSQL("DROP table if EXISTS playlist");
        db.execSQL("DROP table if EXISTS playlist_songs");
        // create new tables
        onCreate(db);
    }

    public void InsertRowSong(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("song_id",id);
        db.insert("favourite", null,contentValues);
    }

    public ArrayList getAllFavourite() {

        ArrayList arrayList = new ArrayList();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor result = db.rawQuery("select * from favourite", null);
            result.moveToFirst();
            while (!result.isAfterLast()) {
                Log.d("response", "fff");
                arrayList.add(result.getInt(result.getColumnIndex("song_id")) );
                result.moveToNext();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return arrayList;
    }

    public void deleteFavourite(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from favourite where song_id="+Integer.toString(id));
    }

    public void InsertRowNote(String title, String noteDate, String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title",title);
        contentValues.put("noteDate",noteDate);
        contentValues.put("desc",desc);
        db.insert("music_notes", null,contentValues);
    }

    public ArrayList getAllNotes() {

        ArrayList<note_items> arrayList = new ArrayList();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor result = db.rawQuery("select * from music_notes", null);
            result.moveToFirst();
            while (!result.isAfterLast()) {
                note_items item = new note_items(result.getInt(result.getColumnIndex("id")), result.getString(result.getColumnIndex("title"))
                , result.getString(result.getColumnIndex("noteDate")));

                arrayList.add(item);
                result.moveToNext();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return arrayList;
    }


    public String getNoteContent(int note_id) {

        String content = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor result = db.rawQuery("select desc from music_notes where id ="+note_id , null);
            result.moveToFirst();
            while (!result.isAfterLast()) {
                content = result.getString(result.getColumnIndex("desc"));
                result.moveToNext();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return content;
    }

    public void updateNoteContent(int note_id, String title, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title",title);
        contentValues.put("desc",content);
        String strFilter = "id=" + note_id;
        db.update("music_notes", contentValues,strFilter, null);
    }

    public void deleteNote(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from music_notes where id="+Integer.toString(id));
    }

    public void deleteAllnotes(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from favourite where id > 0");
    }

    public void InsertRowPlaylist(String playlist_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",playlist_name);
        db.insert("playlist", null,contentValues);
    }

    public Boolean checkPlaylist(String playlist_name) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor result;
            result = db.rawQuery("select id from playlist where name ='"+playlist_name+"'", null);
            result.moveToFirst();
            if (!result.isAfterLast()) {
                return true;
            }
            result.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    public ArrayList getAllPlaylists() {

        ArrayList<playlist_items> arrayList = new ArrayList();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor result = db.rawQuery("select * from playlist", null);
            result.moveToFirst();
            while (!result.isAfterLast()) {
                playlist_items item = new playlist_items(result.getInt(result.getColumnIndex("id")), result.getString(result.getColumnIndex("name")));

                arrayList.add(item);
                result.moveToNext();
            }
            result.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return arrayList;
    }

    public void InsertRowPlaylistSongs(int playlist_id, long song_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("playlist_id",playlist_id);
        contentValues.put("song_id",song_id);
        db.insert("playlist_songs", null,contentValues);
        //db.execSQL("insert into playlist_songs (playlist_id, song_id) values ('"+playlist_id+"', '"+song_id+"')");
    }

    public ArrayList getPlaylistSongs(int playlist_id) {

        ArrayList<playlist_items> arrayList = new ArrayList();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor result = db.rawQuery("select * from playlist_songs where playlist_id =='"+playlist_id+"'", null);
            result.moveToFirst();
            while (!result.isAfterLast()) {
                playlist_items item = new playlist_items(result.getInt(result.getColumnIndex("id")),
                        result.getInt(result.getColumnIndex("playlist_id")), result.getInt(result.getColumnIndex("song_id")));

                arrayList.add(item);
                result.moveToNext();
            }
            result.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return arrayList;
    }

    public Boolean checkisSongsinPlaylist(int playlist_id, long song_id) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor result;
            result = db.rawQuery("select id from playlist_songs where playlist_id ='"+playlist_id+"' and song_id ='"+song_id+"'", null);
            result.moveToFirst();
            if (!result.isAfterLast()) {
                return true;
            }
            result.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    public void deleteSongsplaylist(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from playlist_songs where song_id="+Long.toString(id));
    }

    public void deleteAllplaylists(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from playlist where id > 0");
    }

    public void deleteAllplaylistsongs(int playlist_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from playlist_songs where playlist_id > 0");
    }

}
