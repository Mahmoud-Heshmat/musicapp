package com.example.mahmoudheshmat.musicapp;

public class note_items {

    private int note_id;
    private String note_title;
    private String note_date;
    private String note_desc;

    public note_items(int note_id, String note_title, String note_date){
        this.note_id = note_id;
        this.note_title = note_title;
        this.note_date = note_date;
    }

    public int getNote_id() {
        return note_id;
    }

    public void setNote_id(int note_id) {
        this.note_id = note_id;
    }

    public String getNote_title() {
        return note_title;
    }

    public void setNote_title(String note_title) {
        this.note_title = note_title;
    }

    public String getNote_date() {
        return note_date;
    }

    public void setNote_date(String note_date) {
        this.note_date = note_date;
    }

    public String getNote_desc() {
        return note_desc;
    }

    public void setNote_desc(String note_desc) {
        this.note_desc = note_desc;
    }
}
