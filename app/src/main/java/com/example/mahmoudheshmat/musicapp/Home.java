package com.example.mahmoudheshmat.musicapp;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.AudioEffect;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import petrov.kristiyan.colorpicker.ColorPicker;

public class Home extends AppCompatActivity {

    //Drawer
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    Context context;

    //Bottom Drawer
    BottomNavigationView bottomNavigationView;

    //Service get mediaSessionId
    playingsongService mService;
    Boolean isconnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.home));
        context = this;

        //Navigation Drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.drawer_open, R.string.drawer_close );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                NavigationItemSelected(item.getItemId());
                return false;
            }
        });

        // Bottom Navigation Drawer
        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        //bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        BottomNavigationItemSelected(item.getItemId());
                        return false;
                    }
                });

        //context.deleteDatabase("music.db");
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    public void NavigationItemSelected(int selectedItem){
        switch (selectedItem){
            case R.id.theme:
                changeBackground();
                break;

            case R.id.songs:
                NavigationUtils.navigateSongs(context);
                break;

            case R.id.playlist:
                NavigationUtils.navigatoPlaylists(context);
                break;

            case R.id.favourite:
                favouritesFragment favourites = new favouritesFragment();
                addCenterFragments(favourites, Constant.FAVOURITES);
                break;

            case R.id.equalizer:
                Intent effects = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
                effects.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, mService.getAudioSessionId());
                startActivityForResult(effects, 0);
                break;

            case R.id.notes:
                NavigationUtils.navigatoNotes(context);
                //drawerLayout.closeDrawers();
                break;

            case R.id.setting:

                break;

            case R.id.about:

                break;


        }
    }

    public void BottomNavigationItemSelected(int selectedItem){
        switch (selectedItem){
            case R.id.music:
                NavigationUtils.navigateSongs(context);
                break;
            case R.id.home_page:

                break;
            case R.id.source:
                //sourceFragment source = new sourceFragment();
                //addCenterFragments(source, Constant.SOURCE);
                break;
        }
    }

    private void addCenterFragments(Fragment fragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        Intent intent = new Intent(context, playingsongService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }

    //Connect to bind service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            playingsongService.localBinder local = (playingsongService.localBinder) service;
            mService = local.getService();
            isconnect = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isconnect = false;
        }
    };

    //change color theme background
    public void changeBackground(){
        final ColorPicker colorPicker = new ColorPicker(this);
        colorPicker.show();
        colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position,int color) {
                toolbar.setBackgroundColor(color);
            }

            @Override
            public void onCancel(){
                colorPicker.dismissDialog();
            }
        });
    }

}
