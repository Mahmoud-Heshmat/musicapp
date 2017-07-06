package com.example.mahmoudheshmat.musicapp;

import android.app.SearchManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class searchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private String queryString;
    SearchView mSearchView;
    Toolbar toolbar;

    RecyclerView recyclerView;
    private searchAdapter adapter;
    private List searchResults = Collections.emptyList();
    LinearLayoutManager linearLayoutManager;

    getSongs SongLoader;
    getSongs AlbumLoader;
    getSongs ArtistLoader;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AlbumLoader = new getSongs();
        ArtistLoader = new getSongs();
        SongLoader = new getSongs();

        context = this;

        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_viewFragment);

        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new searchAdapter(this, mSearchView);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);

        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));

        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint(getString(R.string.search_library));

        mSearchView.setIconifiedByDefault(false);
        mSearchView.setIconified(false);


        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.menu_search), new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                finish();
                return false;
            }
        });

        menu.findItem(R.id.menu_search).expandActionView();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        if (newText.equals(queryString)) {
            return true;
        }
        queryString = newText;
        if (queryString.trim().equals("")) {
            searchResults.clear();
            adapter.updateSearchResultsF();
        } else {
            List<type_song> songList = SongLoader.search_songs(context, queryString);
            List<type_album> albumList = AlbumLoader.search_albums(context, queryString);
            List<type_artist> artistList = ArtistLoader.search_artists(context,queryString);

            adapter.updateSearchResults(songList, albumList, artistList);
        }

        return true;
    }

}
