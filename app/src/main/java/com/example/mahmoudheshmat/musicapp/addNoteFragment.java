package com.example.mahmoudheshmat.musicapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class addNoteFragment extends android.support.v4.app.Fragment{

    EditText note_title;
    EditText note_content;

    String title;
    String content;
    String date;

    MenuItem item;

    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_add_note, container, false);

        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        if (toolbar != null) {
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        }
        toolbar.setTitle(R.string.add_note);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities utilities = new Utilities();
                title = note_title.getText().toString();
                content = note_content.getText().toString();
                date = utilities.getCurrentTimeStamp();

                if(content.isEmpty()){
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.container)).commit();
                }else{
                    SqltieDatabase sql = new SqltieDatabase(getContext());
                    if(title.isEmpty()){
                        title = "No title";
                    }
                    sql.InsertRowNote(title, date, content);
                    NavigationUtils.navigatoNotes(getContext());
                }
            }
        });

        note_title = (EditText) rootView.findViewById(R.id.note_title);
        note_content = (EditText) rootView.findViewById(R.id.note_content);

        return rootView;
    }


}