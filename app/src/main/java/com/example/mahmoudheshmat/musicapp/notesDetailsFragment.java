package com.example.mahmoudheshmat.musicapp;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class notesDetailsFragment extends android.support.v4.app.Fragment{

    EditText note_title;
    EditText note_content;
    TextView note_date;

    Toolbar toolbar;

    int id;
    String title;
    String content;
    String date;

    SqltieDatabase sql;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_note_details, container, false);

        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        if (toolbar != null) {
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        }
        toolbar.setTitle(R.string.note_detail);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);


        note_title = (EditText) rootView.findViewById(R.id.note_title);
        note_content = (EditText) rootView.findViewById(R.id.note_content);
        note_date = (TextView) rootView.findViewById(R.id.note_date);

        Bundle bundle = getArguments();
        if(bundle != null){
            id = bundle.getInt("note_id");
            title = bundle.getString("note_title");
            date = bundle.getString("note_date");

            sql = new SqltieDatabase(getContext());
            content = sql.getNoteContent(id);

            note_title.setText(title);
            note_date.setText("Created  " + date);
            note_content.setText(content);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.equals(note_title.getText().toString()) && content.equals(note_content.getText().toString()) ){
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.container)).commit();
                }else{
                    sql.updateNoteContent(id, note_title.getText().toString(), note_content.getText().toString());
                    NavigationUtils.navigatoNotes(getContext());
                }
            }
        });

        return rootView;
    }



}
